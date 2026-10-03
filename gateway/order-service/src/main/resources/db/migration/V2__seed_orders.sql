INSERT INTO orders (id, product_id, quantity, unit_price, total_price)
VALUES (1, 1, 3, 1500.50, 4501.50);

INSERT INTO orders (id, product_id, quantity, unit_price, total_price)
VALUES (2, 2, 10, 25.00, 250.00);

-- Đẩy identity vượt qua id vừa chèn thủ công, tránh trùng khóa khi Hibernate insert tiếp
ALTER TABLE orders ALTER COLUMN id RESTART WITH 3;
