# CNPM
# 🛍️ Đồ án Công nghệ phần mềm — Hệ thống FastFood (Microservices)

## 👩‍💻 Thành viên thực hiện
- **Nguyễn Ngọc Thúy Vy**  
- **Nguyễn Thị Thu Hường**

---

## 🧱 Giới thiệu
Dự án được xây dựng theo kiến trúc **Microservice**, bao gồm các dịch vụ chính:
- `eureka_server`: Service registry (Spring Cloud Netflix Eureka)
- `user_service`: Quản lý người dùng
- `order_service`: Quản lý đơn hàng
- `product_service`: Quản lý sản phẩm
- `payment_service`: Xử lý thanh toán

---

## ⚙️ Công nghệ sử dụng
- **Back-end**: Spring Boot, Spring Cloud, Spring Security, Feign Client  
- **Cơ sở dữ liệu**: PostgreSQL  
- **Build tool**: Maven  
- **Triển khai CI/CD**: GitHub Actions  
- **Frontend**: ReactJS (Web) và React Native (Mobile)

---

## 🚀 Cách chạy project
1. Cài **Java 21** và **Maven**
2. Chạy **Eureka Server** trước:
   ```bash
   cd eureka_server
   mvn spring-boot:run
