# CNPM
# 🍱 FoodFast Delivery – Drone-based Food Delivery Service

## 🧩 Giới thiệu

**FoodFast Delivery** là hệ thống giao đồ ăn sử dụng **drone tự động** được phát triển theo **kiến trúc microservices**, kết hợp giữa **ứng dụng web** và **mobile app**.  
Dự án được xây dựng trong học phần **Công nghệ phần mềm – Trường Đại học Sài Gòn**, với mục tiêu **tự động hóa quy trình giao hàng**, **tối ưu thời gian**, và **giảm chi phí vận hành**.

---

## 👩‍💻 Nhóm thực hiện

| MSSV | Họ tên | Vai trò |
|------|--------|----------|
| 3122411079 | Nguyễn Thị Thu Hường |
| 3122411256 | Nguyễn Ngọc Thúy Vy  |

---

## 🚀 Mục tiêu dự án

- Tự động hóa quá trình **đặt món – giao hàng – thanh toán** bằng drone.  
- Cung cấp khả năng **theo dõi hành trình drone theo thời gian thực**.  
- Tích hợp **thanh toán điện tử MoMo**.  
- Mô phỏng **quy trình vận hành thực tế** bằng các công nghệ hiện đại: Spring Boot, WebSocket, Docker, Leaflet.js, PostgreSQL.  
- Hỗ trợ triển khai theo mô hình **CI/CD với GitHub Actions và DockerHub**.

---

## 🏗️ Kiến trúc hệ thống

Hệ thống được thiết kế theo mô hình **Microservices**, gồm các thành phần chính:

| Service | Port | Mô tả |
|----------|------|--------|
| **Eureka Server** | `8761` | Trung tâm đăng ký dịch vụ (Service Discovery) |
| **API Gateway** | `8085` | Tiếp nhận & định tuyến request đến các service |
| **User Service** | `8081` | Quản lý người dùng, nhà hàng, phân quyền |
| **Product Service** | `8080` | Quản lý danh mục món ăn |
| **Order Service** | `8082` | Xử lý và lưu trữ đơn hàng |
| **Restaurant Service** | `8083` | Quản lý thông tin nhà hàng và xác nhận đơn |
| **Payment Service** | `8084` | Xử lý thanh toán MoMo |
| **Drone Service** | `8088` | Điều phối và mô phỏng hành trình Drone |
| **PostgreSQL** | `5433` (host) → `5432` (container) | CSDL trung tâm |
| **Frontend (Vite)** | `5173` | Giao diện người dùng (React/Vite) |

---

## 🌐 URL truy cập khi chạy hệ thống

| Thành phần | URL |
|-------------|-----|
| **Eureka Dashboard** | [http://localhost:8761](http://localhost:8761) |
| **API Gateway** | [http://localhost:8085](http://localhost:8085) |
| **User Service** | [http://localhost:8081](http://localhost:8081) |
| **Product Service** | [http://localhost:8080](http://localhost:8080) |
| **Order Service** | [http://localhost:8082](http://localhost:8082) |
| **Restaurant Service** | [http://localhost:8083](http://localhost:8083) |
| **Payment Service** | [http://localhost:8084](http://localhost:8084) |
| **Drone Service** | [http://localhost:8088](http://localhost:8088) |
| **Frontend (Web UI)** | [http://localhost:5173](http://localhost:5173) |
| **Database (PostgreSQL)** | `jdbc:postgresql://localhost:5433/foodfast_db` |

---

## 🧭 Quy trình hoạt động

1. **Khách hàng đặt món** → Ứng dụng gửi request qua `API Gateway`.  
2. **Nhà hàng xác nhận đơn** → Chuẩn bị món & khởi tạo giao hàng bằng drone.  
3. **Drone Service** → Tính toán lộ trình GPS, cập nhật tọa độ qua WebSocket.  
4. **Khách hàng** → Theo dõi drone thời gian thực trên bản đồ (Leaflet.js).  
5. **Thanh toán** → Thực hiện qua **MoMo Payment Gateway (sandbox)**.  
6. **Admin** → Giám sát hệ thống, kiểm tra log và hiệu suất drone.

---

## 💳 Tích hợp thanh toán (MoMo SDK)

- **Công nghệ:** `MoMo Spring Boot SDK`, `RestTemplate`, `PostgreSQL`.  
- **Luồng xử lý:**
  1. Tạo yêu cầu thanh toán tại `Payment Service`.  
  2. Gửi đến `MoMo Gateway`.  
  3. Nhận phản hồi `redirectUrl` và `ipnUrl`.  
  4. Cập nhật trạng thái đơn hàng trong `Order Service`.

---

## 🗺️ Drone Service – Mô phỏng hành trình

- **GPS Tracking:** Drone gửi tọa độ về server mỗi 2 giây (`Spring Scheduler`).  
- **Realtime Communication:** `WebSocket` truyền dữ liệu vị trí.  
- **Bản đồ:** hiển thị hành trình trên **Leaflet.js (OpenStreetMap)**.  
- **Sai số GPS:** ~5–10m → làm tròn tọa độ 5 chữ số thập phân, xác định vùng giao hàng an toàn (bán kính 15m).

---

## 🧮 ERD (Mô hình dữ liệu)

| Thực thể | Mô tả |
|-----------|--------|
| **User** | Khách hàng, nhà hàng, admin |
| **Restaurant** | Quản lý sản phẩm và drone |
| **Product** | Món ăn của nhà hàng |
| **Order / Order_Item** | Đơn hàng và chi tiết món |
| **Payment** | Giao dịch thanh toán |
| **Drone** | Thiết bị giao hàng |
| **Delivery / Delivery_Tracking** | Quản lý và theo dõi chuyến bay |

---

## ⚙️ Công nghệ sử dụng

- **Back-end**: Spring Boot, Spring Cloud, Spring Security, Feign Client  
- **Cơ sở dữ liệu**: PostgreSQL  
- **Build tool**: Maven  
- **Triển khai CI/CD**: GitHub Actions  
- **Frontend**: ReactJS (Web) và React Native (Mobile)

---

## 🏁 Cách chạy dự án (Local)

### 1️⃣ Clone và cài đặt
```bash
git clone https://github.com/nguyenthuyvyy/CNPM.git
cd CNPM
2️⃣ Chạy lần lượt các service

⚠️ Lưu ý: Mỗi service nên mở ở một terminal riêng.

Bước 1: Khởi động Eureka Server
PS D:\cnpm\CNPM-2\DoAnCNPM_Backend\eureka_server> mvn spring-boot:run

🔹 Bước 2: Khởi động API Gateway
PS D:\cnpm\CNPM-2\DoAnCNPM_Backend\api-gateway> mvn spring-boot:run

🔹 Bước 3: Khởi động các service còn lại
PS D:\cnpm\CNPM-2\DoAnCNPM_Backend\user_service> mvn spring-boot:run
PS D:\cnpm\CNPM-2\DoAnCNPM_Backend\product_service> mvn spring-boot:run
PS D:\cnpm\CNPM-2\DoAnCNPM_Backend\order_service> mvn spring-boot:run
PS D:\cnpm\CNPM-2\DoAnCNPM_Backend\restaurant_service> mvn spring-boot:run
PS D:\cnpm\CNPM-2\DoAnCNPM_Backend\payment_service> mvn spring-boot:run
PS D:\cnpm\CNPM-2\DoAnCNPM_Backend\drone_service> mvn spring-boot:run

🔹 Bước 4: Chạy Frontend (Vite)
PS D:\cnpm\CNPM-2\frontend> npm install
PS D:\cnpm\CNPM-2\frontend> npm run dev

3️⃣ Truy cập hệ thống

Frontend: http://localhost:5173

API Gateway: http://localhost:8085

Eureka Server: http://localhost:8761

Database: jdbc:postgresql://localhost:5433/foodfast_db
