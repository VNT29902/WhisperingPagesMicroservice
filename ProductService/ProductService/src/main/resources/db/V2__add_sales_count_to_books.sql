ALTER TABLE books
ADD COLUMN sales_count INT NOT NULL DEFAULT 0;

UPDATE books SET sales_count = 0 WHERE sales_count IS NULL;