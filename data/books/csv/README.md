# CSV import directory

Put your CSV book files here if you want to keep them in Git with the repo.

Recommended naming:
- `books_YYYYMMDD.csv` (example: `books_20260406.csv`)

Import command example:

```bash
./scripts/import_books_csv_to_catalog.py --csv data/books/csv/books_YYYYMMDD.csv --mode merge
./scripts/generate_books_seed_from_catalog.py
```
