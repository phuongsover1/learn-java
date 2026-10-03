INSERT INTO products (id, name, price) VALUES (1, 'Laptop', 1500.50);
INSERT INTO products (id, name, price) VALUES (2, 'Mouse', 25.00);

-- Đẩy identity vượt qua id vừa chèn thủ công, tránh trùng khóa khi Hibernate insert tiếp
ALTER TABLE products ALTER COLUMN id RESTART WITH 3;
