# Database Seeding Playbook (Bookstore-style)

This playbook describes how to seed a realistic bookstore catalog for local development without frontend mock data.

## 1) Source of truth in this repo
- MySQL container loads SQL files from `mysql_init/` on first initialization.
- Product catalog seed is provided in:
  - `mysql_init/dump_product_db.sql` (base dump)
  - `mysql_init/z_seed_fahasa_style_books.sql` (extra categorized sample books)

## 2) Categories included in the bookstore-style seed
- `van-hoc`
- `kinh-te`
- `thieu-nhi`
- `manga-comic`
- `tam-ly-ky-nang`
- `ngoai-ngu`
- `lich-su`
- `khoa-hoc`
- `cong-nghe`

## 3) Reset and seed from scratch
Run from repository root:

```bash
docker compose down -v
rm -rf mysql_data
docker compose up --build -d
```

## 4) Verify seeded data
```bash
docker exec -it mysql mysql -uroot -p${MYSQL_ROOT_PASSWORD:-290902} -e "USE product_db_flyway; SELECT category, COUNT(*) AS total FROM books GROUP BY category ORDER BY total DESC;"
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
