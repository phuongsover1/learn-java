FROM mysql:8.0

ENV MYSQL_ROOT_PASSWORD=root

# Copy script SQL vào thư mục init của MySQL
COPY init.sql /docker-entrypoint-initdb.d/