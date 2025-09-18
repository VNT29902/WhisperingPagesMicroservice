CREATE TABLE book_search_index (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    book_id VARCHAR(20) NOT NULL,
    title_no_accent VARCHAR(255) NOT NULL,
    author_no_accent VARCHAR(255) NOT NULL,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_book FOREIGN KEY (book_id) REFERENCES books(id) ON DELETE CASCADE
);

CREATE INDEX idx_book_search_title ON book_search_index(title_no_accent);
CREATE INDEX idx_book_search_author ON book_search_index(author_no_accent);