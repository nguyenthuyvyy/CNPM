# 🚀 K6 Quick Start Guide - CNPM

## 5-Minute Setup

### Step 1: Install k6

**Windows (Chocolatey):**
```powershell
choco install k6
```

**Mac (Homebrew):**
```bash
brew install k6
```

**Linux (apt):**
```bash
sudo apt-get update && sudo apt-get install -y k6
```

### Step 2: Start Dashboard (Optional)

```powershell
# Windows
cd D:\cnpm\CNPM-3\k6
.\scripts\run-tests.bat
# Select option 1

# Linux/Mac
cd path/to/k6
chmod +x scripts/run-tests.sh
./scripts/run-tests.sh
# Select option 1
```

Wait for Docker containers to start (30-60 seconds)

### Step 3: Run Tests

```powershell
# Windows
k6 run --out influxdb=http://localhost:8086/k6 `
  .\tests\user-service-load.js

# Linux/Mac
k6 run --out influxdb=http://localhost:8086/k6 \
  ./tests/user-service-load.js
```

### Step 4: View Results

Open dashboard:
```
http://localhost:3000
```

**Credentials:**
- Username: admin
- Password: admin

---

## 📊 What's Being Tested

### User Service (Port 8081)
- Register users
- Login with JWT
- Get user info
- List all users

### Product Service (Port 8085)
- List all products
- Get product by ID
- Search products

### Order Service (Port 8082)
- Create orders
- List orders
- Get order details

---

## 🎯 Key Metrics to Watch

| Metric | Good | Warning | Critical |
|--------|------|---------|----------|
| p95 Response | <1s | 1-2s | >2s |
| p99 Response | <1.5s | 1.5-3s | >3s |
| Error Rate | <1% | 1-5% | >5% |
| Success Rate | >99% | 95-99% | <95% |

---

## ⚡ Common Commands

```bash
# Run single test
k6 run ./tests/user-service-load.js

# Run with custom URL
k6 run --env BASE_URL=http://api.example.com:8081 ./tests/user-service-load.js

# Run with specific user count and duration
k6 run -u 100 -d 5m ./tests/user-service-load.js

# Save results to JSON
k6 run --out json=results.json ./tests/user-service-load.js

# Run all tests sequentially
for test in tests/*.js; do k6 run $test; done
```

---

## 📈 Dashboard URL Map

| Component | URL |
|-----------|-----|
| Grafana | http://localhost:3000 |
| InfluxDB | http://localhost:8086 |
| Prometheus | http://localhost:9090 |

---

## 🛑 Stop Dashboard

```powershell
# Windows
docker-compose -f docker-compose.k6.yml down

# Linux/Mac
docker-compose -f docker-compose.k6.yml down
```

---

## 💡 Tips & Tricks

1. **Real-time Monitoring**: Keep Grafana dashboard open while running tests
2. **Multiple Terminals**: Run dashboard in one terminal, tests in another
3. **Compare Results**: Run same test multiple times to track trends
4. **Adjust Load**: Modify `stages` in test files to simulate different scenarios
5. **Export Data**: Use `--out json` to export and analyze results

---

## ❓ Need Help?

- Check dashboard for real-time metrics
- Review test file comments for details
- See main `README.md` for advanced configuration
- Check k6 docs: https://k6.io/docs/

---

**Status:** ✅ Ready to test!

Run your first test now:
```
k6 run ./tests/user-service-load.js
```
