# K6 Performance Testing Suite for CNPM

Comprehensive load and performance testing suite for CNPM Fast Food Delivery microservices with automatic Grafana dashboard visualization.

## 📋 Table of Contents

- [Overview](#overview)
- [Installation](#installation)
- [Quick Start](#quick-start)
- [Test Scenarios](#test-scenarios)
- [Dashboard](#dashboard)
- [Configuration](#configuration)
- [Advanced Usage](#advanced-usage)

---

## 🎯 Overview

This k6 testing suite provides:

- **Load Testing**: Simulate realistic user load across services
- **Performance Metrics**: Automatic collection and visualization
- **Real-time Dashboard**: Live monitoring with Grafana
- **Custom Metrics**: Service-specific performance indicators
- **Multi-service Testing**: User, Product, and Order services
- **Automated Reporting**: Visual reports with thresholds

### Services Covered

| Service | Test File | Port | Scenarios |
|---------|-----------|------|-----------|
| User Service | `user-service-load.js` | 8081 | Register, Login, Get Users, Create User |
| Product Service | `product-service-load.js` | 8085 | List Products, Get Product, Search |
| Order Service | `order-service-load.js` | 8082 | Create Order, List Orders, Get Order |

---

## 📦 Installation

### Prerequisites

- **Docker & Docker Compose** (for dashboard)
- **k6** (load testing tool)

### Windows Installation

#### 1. Install k6

```powershell
# Using Chocolatey
choco install k6

# Or download from
# https://k6.io/docs/getting-started/installation
```

#### 2. Verify Installation

```powershell
k6 version
```

#### 3. Verify Docker

```powershell
docker --version
docker-compose --version
```

### Linux/Mac Installation

```bash
# Using Homebrew
brew install k6

# Verify
k6 version
```

---

## 🚀 Quick Start

### Windows

1. **Navigate to k6 directory:**
   ```powershell
   cd D:\cnpm\CNPM-3\k6
   ```

2. **Run the test script:**
   ```powershell
   .\scripts\run-tests.bat
   ```

3. **Select option from menu:**
   ```
   1) Start Dashboard (InfluxDB + Grafana)
   2) Run All Load Tests
   ```

### Linux/Mac

1. **Navigate to k6 directory:**
   ```bash
   cd path/to/CNPM-3/k6
   ```

2. **Make script executable:**
   ```bash
   chmod +x scripts/run-tests.sh
   ```

3. **Run the test script:**
   ```bash
   ./scripts/run-tests.sh
   ```

---

## 📊 Test Scenarios

### 1. User Service Load Test

**File:** `tests/user-service-load.js`

**Load Profile:**
- Phase 1 (0-30s): Ramp up to 20 users
- Phase 2 (30-90s): Ramp up to 50 users
- Phase 3 (90-210s): Stay at 50 users
- Phase 4 (210-240s): Ramp down to 0 users

**Test Cases:**
1. **Register User** - Create new user accounts
2. **Login User** - Authenticate with credentials
3. **Get All Users** - Retrieve user list
4. **Get User by ID** - Retrieve specific user
5. **Create User** - Admin user creation

**Metrics Tracked:**
- Registration duration
- Login duration
- User retrieval time
- Success/Error rates

### 2. Product Service Load Test

**File:** `tests/product-service-load.js`

**Load Profile:**
- Phase 1 (0-30s): Ramp up to 30 users
- Phase 2 (30-150s): Ramp up to 50 users
- Phase 3 (150-270s): Stay at 50 users
- Phase 4 (270-300s): Ramp down to 0 users

**Test Cases:**
1. **Get All Products** - Retrieve product catalog
2. **Get Product by ID** - Retrieve specific product
3. **Search Products** - Search functionality

**Metrics Tracked:**
- Product list response time
- Product detail response time
- Search performance
- Cache hit rates

### 3. Order Service Load Test

**File:** `tests/order-service-load.js`

**Load Profile:**
- Phase 1 (0-30s): Ramp up to 25 users
- Phase 2 (30-150s): Ramp up to 40 users
- Phase 3 (150-270s): Stay at 40 users
- Phase 4 (270-300s): Ramp down to 0 users

**Test Cases:**
1. **Create Order** - Create new orders with items
2. **Get All Orders** - Retrieve order list
3. **Get Order by ID** - Retrieve specific order

**Metrics Tracked:**
- Order creation time
- Order retrieval time
- Transaction success rate
- Payment processing time

---

## 📈 Dashboard

### Access Dashboard

After starting the dashboard, navigate to:

```
http://localhost:3000
```

**Default Credentials:**
- Username: `admin`
- Password: `admin`

### Available Metrics

#### Real-time Graphs

1. **Request Duration**
   - p95 response time
   - p99 response time
   - Average response time

2. **Throughput**
   - Requests per second
   - Successful requests
   - Failed requests

3. **Error Rates**
   - Error percentage
   - 4xx errors
   - 5xx errors

4. **Virtual Users**
   - Active VUs over time
   - Ramp up/down timeline
   - Peak load period

#### Custom Metrics

- `login_duration` - Login endpoint performance
- `get_user_duration` - User retrieval time
- `create_user_duration` - User creation time
- `product_list_duration` - Product list loading
- `product_detail_duration` - Product detail loading
- `order_create_duration` - Order creation time
- `order_list_duration` - Order list loading
- `order_detail_duration` - Order detail loading

---

## ⚙️ Configuration

### Environment Variables

```powershell
# Windows - Set before running tests
$env:BASE_URL="http://localhost:8081"

# Linux/Mac - Set before running tests
export BASE_URL="http://localhost:8081"
```

### Threshold Configuration

Edit test files to adjust thresholds:

```javascript
thresholds: {
  http_req_duration: ['p(99)<1500', 'p(95)<1000'],
  http_req_failed: ['rate<0.1'],
  errors: ['rate<0.1'],
}
```

**Meanings:**
- `p(99)<1500` - 99th percentile must be < 1500ms
- `p(95)<1000` - 95th percentile must be < 1000ms
- `rate<0.1` - Error rate must be < 10%

### Test Duration

Modify stages in test files:

```javascript
stages: [
  { duration: '30s', target: 20 },   // 30 seconds to reach 20 VUs
  { duration: '2m', target: 50 },    // 2 minutes to reach 50 VUs
  { duration: '2m', target: 50 },    // Stay at 50 VUs for 2 minutes
  { duration: '30s', target: 0 },    // 30 seconds to ramp down
]
```

---

## 🔧 Advanced Usage

### Run Single Test with Custom URL

```powershell
# Windows
k6 run --out influxdb=http://localhost:8086/k6 `
  --env BASE_URL="http://custom-api.com:8081" `
  .\tests\user-service-load.js
```

```bash
# Linux/Mac
k6 run --out influxdb=http://localhost:8086/k6 \
  --env BASE_URL="http://custom-api.com:8081" \
  ./tests/user-service-load.js
```

### Run Test Without Dashboard

```powershell
# Windows - Console output only
k6 run --out json=results.json .\tests\user-service-load.js

# Linux/Mac
k6 run --out json=results.json ./tests/user-service-load.js
```

### Run Test with Custom VU Count

```powershell
# Windows - Override stages
k6 run -u 100 -d 5m .\tests\user-service-load.js
```

### Generate Report

```powershell
# Run test and generate HTML report
k6 run --out json=results.json .\tests\user-service-load.js

# Convert to HTML (requires jq)
# See https://k6.io/docs/results-visualization/
```

---

## 📊 Performance Benchmarks

### Expected Thresholds

| Service | Metric | Target | Notes |
|---------|--------|--------|-------|
| User Service | p95 response | <1000ms | Auth operations are fast |
| User Service | p99 response | <1500ms | Including network latency |
| Product Service | p95 response | <1200ms | Database queries involved |
| Product Service | p99 response | <2000ms | Catalog can be large |
| Order Service | p95 response | <2000ms | Complex business logic |
| Order Service | p99 response | <3000ms | Payment processing |
| All Services | Error rate | <10% | Should be mostly successful |

---

## 🐛 Troubleshooting

### k6 Command Not Found

```powershell
# Add to PATH or use full path
C:\Program Files\k6\k6.exe run .\tests\user-service-load.js
```

### Cannot Connect to InfluxDB

```powershell
# Verify Docker is running
docker ps

# Check InfluxDB container
docker logs influxdb-k6

# Verify port is open
netstat -ano | findstr :8086
```

### Grafana Shows No Data

```powershell
# Check k6 output configuration
# Ensure InfluxDB datasource is configured
# Wait 30 seconds for data ingestion

# Verify InfluxDB has data
# curl -X GET http://localhost:8086/query?db=k6&q=SHOW MEASUREMENTS
```

### Services Not Responding

```powershell
# Check if backend services are running
docker ps

# Verify service ports
netstat -ano | findstr :808[1-5]

# Try to access service directly
Invoke-WebRequest http://localhost:8081/health
```

---

## 📚 Resources

- [K6 Official Documentation](https://k6.io/docs/)
- [K6 HTTP Module](https://k6.io/docs/javascript-api/k6-http/)
- [K6 Metrics](https://k6.io/docs/javascript-api/k6-metrics/)
- [Grafana Dashboard Docs](https://grafana.com/docs/)
- [InfluxDB Time Series Database](https://docs.influxdata.com/)

---

## 🤝 Contributing

To add new test scenarios:

1. Create new test file in `tests/` directory
2. Follow naming convention: `{service}-load.js`
3. Include appropriate thresholds and metrics
4. Update `run-tests.bat` or `run-tests.sh`
5. Document in this README

---

## 📝 Test Results Storage

Test results are stored in InfluxDB and can be:

- **Viewed in Grafana** - Real-time during tests
- **Exported to JSON** - `k6 run --out json=results.json`
- **Queried from InfluxDB** - Direct database access
- **Compared over time** - Track performance trends

---

## 🚀 Next Steps

1. ✅ Start dashboard: `Option 1` in menu
2. ✅ Ensure services are running on correct ports
3. ✅ Run single test: `Option 3/4/5` in menu
4. ✅ Monitor Grafana dashboard: http://localhost:3000
5. ✅ Analyze results and optimize

---

**Last Updated:** November 22, 2025  
**k6 Version:** Latest  
**Status:** Ready for production performance testing
