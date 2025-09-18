CREATE TABLE orders (
    id VARCHAR(50) PRIMARY KEY,
    user_name VARCHAR(100) NOT NULL,
    status VARCHAR(30) NOT NULL,
    payment_method VARCHAR(30) NOT NULL,
    total_amount DECIMAL(19,2) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    expires_at TIMESTAMP NULL
);
