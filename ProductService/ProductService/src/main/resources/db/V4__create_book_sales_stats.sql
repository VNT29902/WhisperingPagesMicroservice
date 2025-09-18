CREATE TABLE book_sales_stats (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,

    -- FK tới bảng books
    book_id VARCHAR(20) NOT NULL,

    year INT NOT NULL,
    month INT NOT NULL,
    week INT NOT NULL,

    sales_count INT NOT NULL DEFAULT 0,

    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
        ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT fk_book_sales_book
        FOREIGN KEY (book_id) REFERENCES books(id)
);
