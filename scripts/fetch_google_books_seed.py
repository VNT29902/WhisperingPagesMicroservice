#!/usr/bin/env python3
import argparse
import json
import re
import urllib.parse
import urllib.request
from pathlib import Path

MOCK_ITEMS = [
    {"id": "zyTCAlFPjgYC", "volumeInfo": {"title": "The Google Story", "authors": ["David A. Vise"], "categories": ["Business & Economics"], "description": "Inside the growth of Google.", "imageLinks": {"thumbnail": "https://books.google.com/books/content?id=zyTCAlFPjgYC&printsec=frontcover&img=1&zoom=1"}}},
    {"id": "uW2qDwAAQBAJ", "volumeInfo": {"title": "Atomic Habits", "authors": ["James Clear"], "categories": ["Self-Help"], "description": "An easy proven way to build good habits.", "imageLinks": {"thumbnail": "https://books.google.com/books/content?id=uW2qDwAAQBAJ&printsec=frontcover&img=1&zoom=1"}}},
    {"id": "7HlyDwAAQBAJ", "volumeInfo": {"title": "Sapiens", "authors": ["Yuval Noah Harari"], "categories": ["History"], "description": "A brief history of humankind.", "imageLinks": {"thumbnail": "https://books.google.com/books/content?id=7HlyDwAAQBAJ&printsec=frontcover&img=1&zoom=1"}}},
    {"id": "fFCjDQAAQBAJ", "volumeInfo": {"title": "Clean Code", "authors": ["Robert C. Martin"], "categories": ["Computers"], "description": "A handbook of agile software craftsmanship.", "imageLinks": {"thumbnail": "https://books.google.com/books/content?id=fFCjDQAAQBAJ&printsec=frontcover&img=1&zoom=1"}}},
    {"id": "s1gVAAAAYAAJ", "volumeInfo": {"title": "The Time Machine", "authors": ["H. G. Wells"], "categories": ["Fiction"], "description": "Classic science fiction novel.", "imageLinks": {"thumbnail": "https://books.google.com/books/content?id=s1gVAAAAYAAJ&printsec=frontcover&img=1&zoom=1"}}},
    {"id": "qz2xDwAAQBAJ", "volumeInfo": {"title": "Thinking, Fast and Slow", "authors": ["Daniel Kahneman"], "categories": ["Psychology"], "description": "Two systems that drive the way we think.", "imageLinks": {"thumbnail": "https://books.google.com/books/content?id=qz2xDwAAQBAJ&printsec=frontcover&img=1&zoom=1"}}},
    {"id": "NnQqDwAAQBAJ", "volumeInfo": {"title": "Deep Work", "authors": ["Cal Newport"], "categories": ["Self-Help"], "description": "Rules for focused success in a distracted world.", "imageLinks": {"thumbnail": "https://books.google.com/books/content?id=NnQqDwAAQBAJ&printsec=frontcover&img=1&zoom=1"}}},
    {"id": "t6o0EAAAQBAJ", "volumeInfo": {"title": "The Pragmatic Programmer", "authors": ["David Thomas", "Andrew Hunt"], "categories": ["Computers"], "description": "Your journey to mastery.", "imageLinks": {"thumbnail": "https://books.google.com/books/content?id=t6o0EAAAQBAJ&printsec=frontcover&img=1&zoom=1"}}},
    {"id": "xGgzEAAAQBAJ", "volumeInfo": {"title": "Dune", "authors": ["Frank Herbert"], "categories": ["Fiction"], "description": "Epic science fiction classic.", "imageLinks": {"thumbnail": "https://books.google.com/books/content?id=xGgzEAAAQBAJ&printsec=frontcover&img=1&zoom=1"}}},
    {"id": "u8e8DwAAQBAJ", "volumeInfo": {"title": "Educated", "authors": ["Tara Westover"], "categories": ["Biography & Autobiography"], "description": "A memoir by Tara Westover.", "imageLinks": {"thumbnail": "https://books.google.com/books/content?id=u8e8DwAAQBAJ&printsec=frontcover&img=1&zoom=1"}}}
]

CATEGORY_MAP = {
    "business": "kinh-doanh",
    "economics": "kinh-doanh",
    "fiction": "van-hoc",
    "history": "lich-su",
    "computer": "cong-nghe",
    "technology": "cong-nghe",
    "psychology": "tam-ly-ky-nang",
    "self-help": "tam-ly-ky-nang",
    "biography": "van-hoc",
    "language": "ngoai-ngu",
    "art": "nghe-thuat",
    "science": "khoa-hoc",
}

def slugify(value: str) -> str:
    value = value.lower()
    value = re.sub(r"[^a-z0-9]+", "-", value)
    return value.strip("-") or "ngoai-ngu"


def map_category(categories):
    if not categories:
        return "ngoai-ngu"
    text = " ".join(categories).lower()
    for key, mapped in CATEGORY_MAP.items():
        if key in text:
            return mapped
    return slugify(categories[0])


def fetch_google_books(query: str, max_results: int):
    params = urllib.parse.urlencode({
        "q": query,
        "maxResults": max_results,
        "printType": "books",
    })
    url = f"https://www.googleapis.com/books/v1/volumes?{params}"
    with urllib.request.urlopen(url, timeout=20) as response:
        return json.load(response).get("items", [])


def to_project_records(items):
    records = []
    for idx, item in enumerate(items, start=1):
        vi = item.get("volumeInfo", {})
        cat = map_category(vi.get("categories", []))
        image = (vi.get("imageLinks", {}) or {}).get("thumbnail", "")
        if image.startswith("http://"):
            image = "https://" + image[len("http://"):]
        record = {
            "id": f"GB-{item.get('id', f'X{idx:04d}')[:20].upper()}",
            "title": vi.get("title", f"Google Book {idx}"),
            "author": ", ".join(vi.get("authors", ["Unknown"]))[:255],
            "image": image or f"https://dummyimage.com/400x600/e2e8f0/1e293b&text=google-book-{idx}",
            "price": 99000 + idx * 7000,
            "category": cat,
            "stock": 50 + idx,
            "description": (vi.get("description") or "Google Books sample record")[:500],
            "sales_count": 0,
        }
        records.append(record)
    return records


def write_sql(records, output_sql: Path):
    def esc(s: str) -> str:
        return s.replace("'", "''")

    rows = []
    search_rows = []
    for r in records:
        rows.append(
            f"('{esc(r['id'])}','{esc(r['title'])}','{esc(r['author'])}','{esc(r['image'])}',{r['price']},'{esc(r['category'])}',{r['stock']},'{esc(r['description'])}',NOW(),NOW(),{r['sales_count']})"
        )
        title_no_accent = re.sub(r"[^A-Za-z0-9 ]+", " ", r["title"]).upper().strip()
        author_no_accent = re.sub(r"[^A-Za-z0-9 ]+", " ", r["author"]).upper().strip()
        search_rows.append(f"('{esc(r['id'])}','{esc(title_no_accent)}','{esc(author_no_accent)}')")

    sql = "\n".join([
        "USE product_db_flyway;",
        "",
        "-- Google Books sample (10 records)",
        "INSERT INTO books (id, title, author, image, price, category, stock, description, created_at, updated_at, sales_count)",
        "VALUES",
        ",\n".join(rows),
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
    output_sql.write_text(sql)


def main():
    parser = argparse.ArgumentParser(description="Fetch 10 Google Books records and map to project book schema")
    parser.add_argument("--query", default="subject:fiction", help="Google Books query")
    parser.add_argument("--max-results", type=int, default=10)
    parser.add_argument("--mock", action="store_true", help="Use embedded mock Google Books response")
    parser.add_argument("--out-json", default="docs/samples/google_books_10_project_records.json")
    parser.add_argument("--out-sql", default="mysql_init/z_seed_google_books_10.sql")
    args = parser.parse_args()

    items = MOCK_ITEMS if args.mock else fetch_google_books(args.query, args.max_results)
    records = to_project_records(items[: args.max_results])

    out_json = Path(args.out_json)
    out_sql = Path(args.out_sql)
    out_json.parent.mkdir(parents=True, exist_ok=True)
    out_sql.parent.mkdir(parents=True, exist_ok=True)

    out_json.write_text(json.dumps(records, ensure_ascii=False, indent=2))
    write_sql(records, out_sql)
    print(f"Generated {len(records)} records -> {out_json} and {out_sql}")


if __name__ == "__main__":
    main()
