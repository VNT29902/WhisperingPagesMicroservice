# Database Seeding Playbook (Bookstore-style)

This playbook describes how to seed a realistic bookstore catalog for local development without frontend mock data.

## 1) Source of truth in this repo
- **Single source for book data:** `data/books/books_catalog.json`.
- SQL seed for books is generated from this file using:
  - `./scripts/generate_books_seed_from_catalog.py`
- Generated output:
  - `mysql_init/z_seed_books_catalog.sql`
- MySQL container loads SQL files from `mysql_init/` on first initialization.

## 2) Categories included in the bookstore-style seed
- `van-hoc`
- `kinh-doanh`
- `thieu-nhi`
- `manga-comic`
- `tam-ly-ky-nang`
- `ngoai-ngu`
- `lich-su`
- `khoa-hoc`
- `cong-nghe`
- `nghe-thuat`

## 3) Reset and seed from scratch
Run from repository root:

```bash
docker compose down -v
rm -rf mysql_data
docker compose up --build -d
```

If you update `data/books/books_catalog.json`, regenerate SQL before running compose:

```bash
./scripts/generate_books_seed_from_catalog.py
```

## 4) Verify seeded data
```bash
docker exec -it mysql mysql -uroot -p${MYSQL_ROOT_PASSWORD:-290902} -e "USE product_db_flyway; SELECT category, COUNT(*) AS total FROM books GROUP BY category ORDER BY total DESC;"
```

To verify the balanced 100-book seed specifically:

```bash
docker exec -it mysql mysql -uroot -p${MYSQL_ROOT_PASSWORD:-290902} -e "USE product_db_flyway; SELECT category, COUNT(*) AS total FROM books WHERE id LIKE 'BS-%' GROUP BY category ORDER BY category;"
```

## 5) API verification
```bash
curl "http://localhost:8085/api/products?category=all&limit=12&page=0"
curl "http://localhost:8085/api/products/latest"
curl "http://localhost:8085/api/products/best-selling"
```

## 6) Data quality checklist
Before using a new data batch, validate:
- Required fields: `id, title, author, image, price, category, stock`
- No duplicate IDs
- Price > 0, stock >= 0
- Search index entry exists for each book

## 7) Notes
- Current seed is designed for local/dev demo and category realism.
- For production, use a governed source pipeline (ISBN, publisher, metadata normalization, copyright checks).
- If Vietnamese text appears corrupted (mojibake), reset MySQL volume and recreate container so charset/collation settings are applied from `docker-compose.yml`.

## 8) Google Books API sample (10 records)
Generate 10 records mapped to project structure (`books` + `book_search_index`):

```bash
./scripts/fetch_google_books_seed.py --query "subject:fiction" --max-results 10
```

If your environment blocks external network, generate from embedded mock payload:

```bash
./scripts/fetch_google_books_seed.py --mock --max-results 10
```

Outputs:
- `docs/samples/google_books_10_project_records.json`
- `mysql_init/z_seed_google_books_10.sql`

## 9) Recommended update workflow (single-file data maintenance)
1. Update only `data/books/books_catalog.json`.
2. Run `./scripts/generate_books_seed_from_catalog.py`.
3. Recreate DB to apply latest seed:
   - `docker compose down -v`
   - `rm -rf mysql_data`
   - `docker compose up --build -d`

## 10) Import from CSV (e.g. your 200-book file)
You can import CSV directly into the single catalog file, no manual SQL editing needed.

```bash
./scripts/import_books_csv_to_catalog.py --csv /path/to/your_200_books.csv --mode merge
./scripts/generate_books_seed_from_catalog.py
```

Notes:
- `--mode merge`: upsert by `id` into existing catalog.
- `--mode replace`: replace the whole catalog with CSV content.
- Required CSV columns: `id,title,author,image,price,category` (other columns are optional).
