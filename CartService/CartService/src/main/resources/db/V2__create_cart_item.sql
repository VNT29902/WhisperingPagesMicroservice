CREATE TABLE cart_items (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
    cart_id VARCHAR(36) NOT NULL,
    product_id VARCHAR(20)  NOT NULL,
    image VARCHAR(200)  NOT NULL,
    title VARCHAR(200)  NOT NULL,
    quantity INT NOT NULL,
    price DECIMAL(19, 2) NOT NULL,
    added_at TIMESTAMP NOT NULL,

    CONSTRAINT fk_cart_item_cart
        FOREIGN KEY (cart_id)
        REFERENCES carts(id)
        ON DELETE CASCADE
);