#!/usr/bin/env python3
import argparse
import csv
import json
import re
from pathlib import Path

REQUIRED_MIN_COLUMNS = {"id", "title", "author", "image", "price", "category"}

CATEGORY_MAP = {
    "business": "kinh-doanh",
    "economics": "kinh-doanh",
    "service": "kinh-doanh",
    "customer": "kinh-doanh",
    "history": "lich-su",
    "science": "khoa-hoc",
    "technology": "cong-nghe",
    "computer": "cong-nghe",
    "manga": "manga-comic",
    "comic": "manga-comic",
    "children": "thieu-nhi",
    "novel": "van-hoc",
    "literature": "van-hoc",
    "language": "ngoai-ngu",
    "art": "nghe-thuat",
    "psychology": "tam-ly-ky-nang",
    "self-help": "tam-ly-ky-nang",
}


def slugify(value: str) -> str:
    slug = re.sub(r"[^a-z0-9]+", "-", value.lower()).strip("-")
    return slug or "van-hoc"


def normalize_category(raw: str) -> str:
    text = (raw or "").strip().lower()
    for k, v in CATEGORY_MAP.items():
        if k in text:
            return v
    return slugify(text)


def to_int(value: str, default: int) -> int:
    try:
        return int(float((value or "").strip()))
    except ValueError:
        return default


def parse_csv(csv_path: Path):
    with csv_path.open(newline="", encoding="utf-8-sig") as f:
        reader = csv.DictReader(f)
        headers = set(reader.fieldnames or [])
        missing = REQUIRED_MIN_COLUMNS - headers
        if missing:
            raise ValueError(f"CSV thiếu cột bắt buộc: {sorted(missing)}")

        records = []
        for idx, row in enumerate(reader, start=1):
            book_id = (row.get("id") or "").strip()
            if not book_id:
                book_id = f"CSV-{idx:05d}"

            record = {
                "id": book_id,
                "title": (row.get("title") or "Untitled Book").strip(),
                "author": (row.get("author") or "Unknown Author").strip(),
                "image": (row.get("image") or "https://dummyimage.com/400x600/e2e8f0/1e293b&text=no-image").strip(),
                "price": to_int(row.get("price", ""), 99000),
                "category": normalize_category(row.get("category", "van-hoc")),
                "stock": to_int(row.get("stock", ""), 50),
                "description": (row.get("description") or "Imported from CSV").strip(),
                "sales_count": to_int(row.get("sales_count") or row.get("sale_stock") or "0", 0),
            }
            records.append(record)
    return records


def load_existing(catalog_path: Path):
    if not catalog_path.exists():
        return {}
    data = json.loads(catalog_path.read_text())
    return {r["id"]: r for r in data}


def main():
    parser = argparse.ArgumentParser(description="Import CSV books into catalog JSON")
    parser.add_argument("--csv", required=True, help="Path to CSV source")
    parser.add_argument("--catalog", default="data/books/books_catalog.json")
    parser.add_argument("--mode", choices=["replace", "merge"], default="merge", help="replace: overwrite catalog, merge: upsert by id")
    args = parser.parse_args()

    csv_path = Path(args.csv)
    catalog_path = Path(args.catalog)

    imported = parse_csv(csv_path)

    if args.mode == "replace":
        final = {r["id"]: r for r in imported}
    else:
        final = load_existing(catalog_path)
        for r in imported:
            final[r["id"]] = r

    output = sorted(final.values(), key=lambda x: x["id"])
    catalog_path.parent.mkdir(parents=True, exist_ok=True)
    catalog_path.write_text(json.dumps(output, ensure_ascii=False, indent=2))

    print(f"Imported {len(imported)} records from CSV")
    print(f"Catalog now has {len(output)} records -> {catalog_path}")
    print("Next step: ./scripts/generate_books_seed_from_catalog.py")


if __name__ == "__main__":
    main()
