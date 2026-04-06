#!/usr/bin/env python3
import argparse
import json
import re
from pathlib import Path


def normalize_text_for_search(value: str) -> str:
    return re.sub(r"[^A-Za-z0-9 ]+", " ", value).upper().strip()


def validate(records):
    required = {"id", "title", "author", "image", "price", "category", "stock", "description", "sales_count"}
    if not records:
        raise ValueError("Catalog is empty. Need at least 1 record to generate SQL.")
    ids = set()
    for idx, r in enumerate(records, start=1):
        missing = required - set(r.keys())
        if missing:
            raise ValueError(f"Record #{idx} missing keys: {sorted(missing)}")
        if r["id"] in ids:
            raise ValueError(f"Duplicate id found: {r['id']}")
        ids.add(r["id"])


def esc(v: str) -> str:
    return v.replace("'", "''")


def build_sql(records):
    book_rows = []
    search_rows = []
    id_values = []

    for r in records:
        book_id = esc(str(r["id"]))
        id_values.append(f"'{book_id}'")
        book_rows.append(
            "('{id}','{title}','{author}','{image}',{price},'{category}',{stock},'{description}',NOW(),NOW(),{sales})".format(
                id=book_id,
                title=esc(str(r["title"])),
                author=esc(str(r["author"])),
                image=esc(str(r["image"])),
                price=int(r["price"]),
                category=esc(str(r["category"])),
                stock=int(r["stock"]),
                description=esc(str(r["description"])),
                sales=int(r.get("sales_count", 0)),
            )
        )
        search_rows.append(
            "('{id}','{title}','{author}')".format(
                id=book_id,
                title=esc(normalize_text_for_search(str(r["title"]))),
                author=esc(normalize_text_for_search(str(r["author"]))),
            )
        )

    in_clause = ", ".join(id_values)

    return "\n".join([
        "USE product_db_flyway;",
        "",
        "-- Auto-generated from data/books/books_catalog.json",
        "-- Keep only books that exist in the current catalog",
        f"DELETE FROM book_search_index WHERE book_id NOT IN ({in_clause});",
        f"DELETE FROM books WHERE id NOT IN ({in_clause});",
        "",
        "INSERT INTO books (id, title, author, image, price, category, stock, description, created_at, updated_at, sales_count)",
        "VALUES",
        ",\n".join(book_rows),
        "ON DUPLICATE KEY UPDATE",
        "  title = VALUES(title),",
        "  author = VALUES(author),",
        "  image = VALUES(image),",
        "  price = VALUES(price),",
        "  category = VALUES(category),",
        "  stock = VALUES(stock),",
        "  description = VALUES(description),",
        "  updated_at = VALUES(updated_at);",
        "",
        "INSERT INTO book_search_index (book_id, title_no_accent, author_no_accent)",
        "VALUES",
        ",\n".join(search_rows),
        "ON DUPLICATE KEY UPDATE",
        "  title_no_accent = VALUES(title_no_accent),",
        "  author_no_accent = VALUES(author_no_accent),",
        "  updated_at = CURRENT_TIMESTAMP;",
        "",
    ])


def main():
    parser = argparse.ArgumentParser(description="Generate SQL seed from single books catalog JSON")
    parser.add_argument("--catalog", default="data/books/books_catalog.json")
    parser.add_argument("--out-sql", default="mysql_init/z_seed_books_catalog.sql")
    args = parser.parse_args()

    catalog_path = Path(args.catalog)
    out_path = Path(args.out_sql)

    records = json.loads(catalog_path.read_text())
    validate(records)

    out_path.parent.mkdir(parents=True, exist_ok=True)
    out_path.write_text(build_sql(records))
    print(f"Generated SQL with {len(records)} records -> {out_path}")


if __name__ == "__main__":
    main()
