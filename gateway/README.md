# Spring Cloud Gateway — Microservices Demo

Hệ thống gồm 3 service Spring Boot, giao tiếp qua API Gateway.

| Service | Port | Vai trò | Công nghệ |
|---|---|---|---|
| [gateway-service](./gateway-service) | 8080 | API Gateway, định tuyến request | Spring Cloud Gateway (WebFlux) |
| [product-service](./product-service) | 8081 | Quản lý sản phẩm | Spring Web + JPA + H2 + Flyway |
| [order-service](./order-service) | 8082 | Quản lý đơn hàng | Spring Web + JPA + H2 + Flyway |

- Spring Boot **4.1.1**, Java **27**, Gradle Wrapper **9.7.1**
- Database: **H2 file** (`./data/productdb`, `./data/orderdb`)
- Migration + seed dữ liệu: **Flyway**

## Yêu cầu môi trường

```bash
java -version   # cần JDK 27
```

## 1. Chạy các service

Mở **3 terminal** riêng biệt, mỗi service chạy foreground (dừng bằng `Ctrl+C`):

```bash
# Terminal 1 — product-service (8081)
cd product-service
./gradlew bootRun
```

```bash
# Terminal 2 — order-service (8082)
cd order-service
./gradlew bootRun
```

```bash
# Terminal 3 — gateway-service (8080)
cd gateway-service
./gradlew bootRun
```

Chờ tới khi thấy log `Started ...Application`. Gateway có thể khởi động trước/sau
các service khác đều được, nhưng request sẽ lỗi nếu backend chưa lên.

### Chạy bằng jar (tuỳ chọn)

```bash
./product-service/gradlew -p product-service bootJar
java -jar product-service/build/libs/product-service-0.0.1-SNAPSHOT.jar
```

> Dùng file `*-0.0.1-SNAPSHOT.jar`, **không** dùng `*-plain.jar`
> (plain jar không có Spring Boot loader, sẽ báo `no main manifest attribute`).

## 2. Dữ liệu seed (Flyway)

Khi service chạy lần đầu, Flyway tự tạo bảng và chèn dữ liệu mẫu:

| Service | Migration | Kết quả |
|---|---|---|
| product-service | `V1__create_products_table.sql` | Tạo bảng `products` |
| | `V2__seed_products.sql` | 2 sản phẩm: Laptop 1500.50, Mouse 25.00 |
| order-service | `V1__create_orders_table.sql` | Tạo bảng `orders` |
| | `V2__seed_orders.sql` | 2 đơn hàng (id 1, 2) |

Seed chỉ chạy **một lần**. Các lần chạy sau Flyway báo
`Schema "PUBLIC" is up to date. No migration necessary.`

Hibernate được cấu hình `ddl-auto: validate` — chỉ kiểm tra schema khớp với entity,
không tự sửa bảng. Muốn đổi cấu trúc bảng thì phải thêm migration mới.

### Reset dữ liệu về seed gốc

```bash
rm -rf product-service/data order-service/data
```

Chạy lại service, Flyway sẽ tạo lại bảng và seed lại từ đầu.

## 3. Gọi API qua Gateway

**Base URL:** `http://localhost:8080`

Gateway dùng filter `PrefixPath=/api` để thêm tiền tố. Vì vậy khi gọi qua gateway
bạn **bỏ tiền tố `/api`** so với gọi trực tiếp:

| Gọi qua gateway | Chuyển tiếp tới backend |
|---|---|
| `http://localhost:8080/products` | `http://localhost:8081/api/products` |
| `http://localhost:8080/orders` | `http://localhost:8082/api/orders` |

Gateway cũng thêm response header `X-Powered-By: Phuong Gateway Service`.

### Endpoint sản phẩm (→ product-service :8081)

| Method | Gateway URL | Mô tả |
|---|---|---|
| GET | `http://localhost:8080/products` | Danh sách sản phẩm |
| GET | `http://localhost:8080/products/{id}` | Chi tiết sản phẩm |
| POST | `http://localhost:8080/products` | Tạo sản phẩm |
| PUT | `http://localhost:8080/products/{id}` | Cập nhật sản phẩm |
| DELETE | `http://localhost:8080/products/{id}` | Xoá sản phẩm |

```bash
# Lấy danh sách
curl http://localhost:8080/products

# Tạo mới — name không được rỗng, price phải > 0
curl -X POST http://localhost:8080/products \
  -H 'Content-Type: application/json' \
  -d '{"name":"Keyboard","price":75.00}'

# Cập nhật
curl -X PUT http://localhost:8080/products/1 \
  -H 'Content-Type: application/json' \
  -d '{"name":"Gaming Laptop","price":1999.99}'

# Xoá
curl -X DELETE http://localhost:8080/products/1
```

### Endpoint đơn hàng (→ order-service :8082)

| Method | Gateway URL | Mô tả |
|---|---|---|
| GET | `http://localhost:8080/orders` | Danh sách đơn hàng |
| GET | `http://localhost:8080/orders/{id}` | Chi tiết đơn hàng |
| POST | `http://localhost:8080/orders` | Tạo đơn hàng |
| DELETE | `http://localhost:8080/orders/{id}` | Xoá đơn hàng |

```bash
# Lấy danh sách
curl http://localhost:8080/orders

# Tạo mới — totalPrice do service tự tính (quantity × unitPrice)
curl -X POST http://localhost:8080/orders \
  -H 'Content-Type: application/json' \
  -d '{"productId":1,"quantity":2,"unitPrice":75.00}'

# Xoá
curl -X DELETE http://localhost:8080/orders/1
```

## 4. Gọi API trực tiếp (không qua Gateway)

| Service | Base URL |
|---|---|
| product-service | `http://localhost:8081/api/products` |
| order-service | `http://localhost:8082/api/orders` |

```bash
curl http://localhost:8081/api/products
curl http://localhost:8082/api/orders
```

## 5. H2 Console

| Service | URL | JDBC URL | User | Password |
|---|---|---|---|---|
| product-service | http://localhost:8081/h2-console | `jdbc:h2:file:./data/productdb` | `sa` | *(trống)* |
| order-service | http://localhost:8082/h2-console | `jdbc:h2:file:./data/orderdb` | `sa` | *(trống)* |

Nhấn **Connect** rồi chạy `SELECT * FROM PRODUCTS;` hoặc `SELECT * FROM ORDERS;`

## 6. Ghi chú & xử lý sự cố

- **Không dùng dấu `/` ở cuối URL qua gateway.** Predicate là `Path=/products/**`
  nên `/products` và `/products/1` hoạt động, còn `/products/` sẽ trả **404**
  (backend không match trailing slash).

- **Không dùng `/api/products` qua gateway** — sẽ **404** vì gateway đã tự thêm
  tiền tố `/api`. Chỉ gọi trực tiếp vào service mới dùng `/api/...`.

- **`/actuator/health` trả 404** — các service khai báo `management.endpoints`
  trong `application.yml` nhưng chưa thêm dependency
  `spring-boot-starter-actuator`.

- **404 / 503 khi gọi qua gateway** — kiểm tra service đích đã chạy chưa:
  ```bash
  ss -ltn | grep -E ':808[0-2]'
  ```

- **Lỗi Flyway `Found non-empty schema(s) ... but no schema history table`** —
  database được tạo bởi Hibernate trước khi có Flyway. Xoá `data/` để tạo lại,
  hoặc thêm vào `application.yml`:
  ```yaml
  spring:
    flyway:
      baseline-on-migrate: true
  ```

- **Thư mục `data/` (file H2) chưa nằm trong `.gitignore`** — nên thêm để tránh
  commit nhầm file database.
