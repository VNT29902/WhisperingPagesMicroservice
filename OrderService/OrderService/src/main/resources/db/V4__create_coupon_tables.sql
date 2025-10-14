CREATE TABLE IF NOT EXISTS coupons (
    id VARCHAR(50) PRIMARY KEY,
    code VARCHAR(100) NOT NULL UNIQUE,
    discount_type VARCHAR(20) NOT NULL,
    discount_value DECIMAL(19,2) NOT NULL,
    currency VARCHAR(10),
    start_at TIMESTAMP NULL,
    end_at TIMESTAMP NULL,
    min_subtotal DECIMAL(19,2),
    max_uses INT,
    max_uses_per_customer INT,
    max_discount_amount DECIMAL(19,2),
    active TINYINT(1) NOT NULL DEFAULT 1,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS coupon_redemptions (
    id VARCHAR(50) PRIMARY KEY,
    coupon_id VARCHAR(50) NOT NULL,
    order_id VARCHAR(50) NOT NULL,
    user_name VARCHAR(100) NOT NULL,
    discount_amount DECIMAL(19,2) NOT NULL,
    redeemed_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_coupon FOREIGN KEY (coupon_id) REFERENCES coupons(id)
);

ALTER TABLE orders
    ADD COLUMN IF NOT EXISTS subtotal_amount DECIMAL(19,2) NOT NULL DEFAULT 0,
    ADD COLUMN IF NOT EXISTS shipping_fee DECIMAL(19,2) NOT NULL DEFAULT 0,
    ADD COLUMN IF NOT EXISTS discount_amount DECIMAL(19,2) NOT NULL DEFAULT 0,
    ADD COLUMN IF NOT EXISTS coupon_code VARCHAR(100);

UPDATE orders
SET subtotal_amount = total_amount,
    shipping_fee = 0,
    discount_amount = 0
WHERE subtotal_amount = 0;
