# Local helper scripts

## restart-local.sh
Restart full local stack after pulling latest code.

```bash
./scripts/restart-local.sh
```

Skip pull if your repo is already updated:

```bash
./scripts/restart-local.sh --skip-pull
```

## generate_books_seed_from_catalog.py
Generate `mysql_init/z_seed_books_catalog.sql` from single source data file:

```bash
./scripts/generate_books_seed_from_catalog.py
```

## import_books_csv_to_catalog.py
Import CSV books into the single catalog file.

```bash
./scripts/import_books_csv_to_catalog.py --csv /path/to/books.csv --mode merge
./scripts/generate_books_seed_from_catalog.py
```
