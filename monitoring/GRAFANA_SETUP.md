# Grafana Monitoring Setup

## Tổng quan
Hệ thống monitoring được tạo bằng **Prometheus** (thu thập metrics) và **Grafana** (hiển thị dashboard).

## Thông tin truy cập

### Grafana
- **URL**: http://localhost:3001
- **Username**: admin
- **Password**: 1admin1

### Prometheus
- **URL**: http://localhost:9090

## Cấu hình tự động

Grafana được cấu hình tự động với:
1. **Datasource**: Prometheus (http://prometheus:9090)
2. **Dashboard**: FastFood Drone - Services Monitoring

## Các Metrics được giám sát

### 1. Response Time (Average)
- Theo dõi thời gian phản hồi trung bình của các API
- Metric: `rate(http_server_requests_seconds_sum[1m]) / rate(http_server_requests_seconds_count[1m])`

### 2. Request Rate (per second)
- Số request xử lý mỗi giây theo từng endpoint
- Metric: `rate(http_server_requests_seconds_count[1m])`

### 3. JVM Memory Usage (MB)
- Bộ nhớ Heap đang sử dụng của các service
- Metric: `jvm_memory_used_bytes{area="heap"} / 1024 / 1024`

### 4. Error Rate (5xx)
- Số lỗi server xảy ra
- Metric: `rate(http_server_requests_seconds_count{status=~"5.."}[1m])`

### 5. CPU Usage (%)
- Phần trăm CPU đang sử dụng
- Metric: `process_cpu_usage * 100`

### 6. Active Threads
- Số thread đang hoạt động trong JVM
- Metric: `jvm_threads_live`

### 7. Services Status
- Trạng thái của các service (Up/Down)
- Metric: `up`
- Green = Up, Red = Down

### 8. Resident Memory (MB)
- Bộ nhớ vật lý đang sử dụng
- Metric: `process_resident_memory_bytes / 1024 / 1024`

## Services được monitor

1. **user-service** (port 8081)
2. **product-service** (port 8088)
3. **order-service** (port 8082)
4. **payment-service** (port 8084)
5. **api-gateway** (port 8085)
6. **drone-service** (port 8086)
7. **restaurant-service** (port 8083)
8. **eureka-server** (port 8761)
9. **node-exporter** (port 9100) - System metrics

## Cấu hình file

- **prometheus.yml**: Cấu hình Prometheus scrapers
- **grafana/provisioning/datasources/prometheus.yml**: Kết nối datasource
- **grafana/provisioning/dashboards/services-dashboard.json**: Dashboard JSON
- **grafana/provisioning/dashboards/dashboard.yml**: Provider cấu hình

## Khởi động hệ thống

```bash
docker-compose up -d --build
```

Chờ ~30 giây để Grafana và Prometheus khởi động hoàn toàn.

## Truy cập dashboard

1. Mở browser: http://localhost:3001
2. Đăng nhập bằng admin/1admin1
3. Chọn dashboard "FastFood Drone - Services Monitoring"

## Cập nhật Grafana Password

Nếu muốn đổi password (mặc định: 1admin1), edit file `docker-compose.yml`:

```yaml
environment:
  - GF_SECURITY_ADMIN_PASSWORD=your_new_password
```

Sau đó chạy lại: `docker-compose up -d`

## Thêm custom alerts (Optional)

Để thêm alerts, tạo file `monitoring/grafana/provisioning/alerting/alerts.yml` và cấu hình rules.

## Troubleshooting

### Grafana không kết nối được Prometheus
- Kiểm tra network: `docker network ls`
- Verify datasource trong Grafana UI → Configuration → Data Sources

### Không có metrics
- Kiểm tra các service có enable Actuator endpoints không
- Verify Prometheus scrape status: http://localhost:9090/targets

### Xóa all Grafana data
```bash
docker volume rm cnpm-3_grafana-data
docker-compose restart grafana
```
