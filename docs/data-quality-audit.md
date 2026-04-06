# Data Quality Audit (Current Git Seed Files)

## Scope
- `data/books/books_catalog.json`
- `mysql_init/z_seed_books_catalog.sql` (generated)
- Legacy seed files (`z_seed_fahasa_style_books.sql`, `z_seed_balanced_100_books.sql`)

## Findings and Fixes

### 1) Placeholder image URLs caused broken covers in UI
- **Issue found:** Existing seed used `https://example.com/...` URLs, which are placeholders and not real images.
- **Fix applied:** Replaced with reachable placeholder image URLs (`dummyimage.com`) in `z_seed_fahasa_style_books.sql`.
- **Impact:** Prevents blank/failed cover images for seeded rows.

### 2) Category naming inconsistency
- **Issue found:** Old seed had `kinh-te` while frontend/category filters expect slug style `kinh-doanh` for business category tabs.
- **Fix applied:** Normalized old seed from `kinh-te` to `kinh-doanh`.
- **Impact:** Better alignment between seeded data and category filtering UX.

### 3) Seed size imbalance
- **Issue found:** Old extra seed had only 24 rows and uneven category distribution.
- **Fix applied:** Added `z_seed_balanced_100_books.sql` with 100 rows, evenly distributed (10 categories × 10 rows).
- **Impact:** Better demo realism and consistent category testing coverage.

## Recommended Validation Queries

```sql
-- Balanced seed distribution
SELECT category, COUNT(*) AS total
FROM books
WHERE id LIKE 'BS-%'
GROUP BY category
ORDER BY category;

-- Check broken image patterns (should be none for new balanced seed)
SELECT id, title, image
FROM books
WHERE id LIKE 'BS-%' AND (image IS NULL OR image = '' OR image NOT LIKE 'http%');
```

## Maintenance rule
- Keep book data updates in **one place only**: `data/books/books_catalog.json`.
- Regenerate SQL by running: `./scripts/generate_books_seed_from_catalog.py`.
