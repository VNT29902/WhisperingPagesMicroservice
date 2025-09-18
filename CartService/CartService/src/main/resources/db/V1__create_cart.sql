CREATE TABLE carts (
 id VARCHAR(36) PRIMARY KEY,
  user_name VARCHAR(100),
  guest_id BINARY(16),
  created_at TIMESTAMP NOT NULL,
  updated_at TIMESTAMP NOT NULL
);


