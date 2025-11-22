# 🚀 K6 + Prometheus + Grafana Integration Guide

## 📋 Quick Overview

K6 tests kết nối trực tiếp với Prometheus → Hiển thị metrics trên Grafana Dashboard

```
K6 Tests → Prometheus Exporter (port 6565) → Prometheus → Grafana Dashboard
```

---

## ⚙️ Setup Steps

### Step 1: Khởi động Infrastructure

```powershell
cd D:\cnpm\CNPM-3
docker-compose up -d
```

**Verify services running:**
```powershell
docker ps
# Should see: postgres, mongodb, eureka, 7 services, prometheus, grafana, node-exporter
```

---

### Step 2: Start Prometheus Exporter

**Terminal 1 - Khởi động exporter:**
```powershell
cd D:\cnpm\CNPM-3
node k6/scripts/k6-prometheus-server.js

# Output:
# 🚀 K6 Prometheus Exporter started
# Port: 6565
# Metrics: http://0.0.0.0:6565/metrics
```

**Verify exporter is running:**
```powershell
curl http://localhost:6565/health

# Response:
# {"status":"ok","timestamp":"...","metrics_updated":"..."}
```

---

### Step 3: Run K6 Tests

**Terminal 2 - Chạy K6 tests:**
```powershell
cd D:\cnpm\CNPM-3

# Full performance test (4 minutes)
k6 run --out json=k6-results.json k6/tests/k6-prometheus.js

# Quick test (1 minute)
k6 run -d 1m -u 5 --out json=k6-results.json k6/tests/k6-prometheus.js
```

**Expected output:**
```
     data_received..................: 45 kB   
     data_sent......................: 38 kB   
     http_req_duration..............: avg=245ms p(95)=520ms p(99)=1.02s
     http_requests..................: 150 
     checks..........................: 100% ✓
```

---

### Step 4: View Metrics in Prometheus

Open: **http://localhost:9090**

**Query metrics:**
```
# Search for K6 metrics
k6_http_requests_total
k6_http_request_errors
k6_http_request_duration_ms
k6_vus
```

**Example query:**
```promql
rate(k6_http_requests_total[1m])  # Requests per minute
```

---

### Step 5: Create Grafana Dashboard

**Open Grafana:** http://localhost:3001
- **Username:** admin
- **Password:** 1admin1

**Create New Dashboard:**

1. Click **"+" → Dashboard**
2. Click **"Add a new panel"**
3. Select **Data source: Prometheus**

**Panel 1: Request Rate**
```
Query: rate(k6_http_requests_total[1m])
Title: Requests Per Minute
Type: Time Series
```

**Panel 2: Error Rate**
```
Query: k6_http_request_errors
Title: Error Rate
Type: Stat
```

**Panel 3: Response Time**
```
Query: k6_http_request_duration_ms
Title: Response Duration (ms)
Type: Time Series
```

**Panel 4: Active VUs**
```
Query: k6_vus
Title: Active Virtual Users
Type: Gauge
```

---

## 🎯 Complete Workflow Example

### Scenario: Chạy load test và monitor kết quả

**1. Terminal 1 - Start Exporter:**
```powershell
cd D:\cnpm\CNPM-3
node k6/scripts/k6-prometheus-server.js
# Wait for "🚀 K6 Prometheus Exporter started"
```

**2. Terminal 2 - Run K6 Test:**
```powershell
cd D:\cnpm\CNPM-3
k6 run --out json=k6-results.json -e BASE_URL=http://localhost:8081 k6/tests/k6-prometheus.js

# Will show:
# ✓ health check passed
# ✓ response time < 500ms
# ✓ status 200
# ...
```

**3. Terminal 3 - Watch Real-time Metrics:**
```powershell
# Query metrics from exporter
$url = 'http://localhost:6565/metrics'
while($true) {
    Clear-Host
    Write-Host "K6 Metrics (updated every 2 seconds)`n" -ForegroundColor Cyan
    (Invoke-WebRequest $url -UseBasicParsing).Content | Select-String 'k6_'
    Start-Sleep 2
}
```

**4. Open Browser - View Dashboard:**
- **Prometheus:** http://localhost:9090
- **Grafana:** http://localhost:3001

Monitor in real-time as tests run!

---

## 🔧 Troubleshooting

### ❌ Exporter không start
```powershell
# Check Node.js installed
node --version

# Check port 6565 available
netstat -ano | findstr :6565

# If port in use, kill process
taskkill /PID <PID> /F
```

### ❌ K6 tests không connect
```powershell
# Verify services running
curl http://localhost:8081/actuator/health
curl http://localhost:8082/actuator/health

# Check BASE_URL
k6 run -e BASE_URL=http://localhost:8081 k6/tests/k6-prometheus.js
```

### ❌ Prometheus không scrape K6
```powershell
# Check Prometheus targets
curl http://localhost:9090/api/v1/targets

# Verify prometheus.yml has k6 job:
# - job_name: "k6"
#   static_configs:
#     - targets: ["localhost:6565"]

# Restart Prometheus
docker-compose restart prometheus
```

### ❌ Grafana không show metrics
```powershell
# Check Prometheus datasource
# Login Grafana → Settings → Data sources → Prometheus
# URL: http://prometheus:9090

# Test query:
# Go to Explore → Metrics browser
# Search: k6_
```

---

## 📊 Prometheus Query Examples

**Performance Metrics:**
```promql
# Average response time
avg(k6_http_request_duration_ms)

# 95th percentile response time
histogram_quantile(0.95, k6_http_request_duration_ms)

# Request rate
rate(k6_http_requests_total[1m])

# Error percentage
(k6_http_request_errors / k6_http_requests_total) * 100

# Active users
k6_vus

# Requests per second
rate(k6_http_requests_total[5s])
```

---

## 🚀 Using Interactive Script

**Windows users can use the automated script:**

```powershell
cd D:\cnpm\CNPM-3
.\k6\scripts\run-k6-test.bat
```

**Menu options:**
```
1. Start Prometheus Exporter
2. Run K6 Tests (full)
3. Run Quick Test (1 min)
4. View Prometheus Metrics
5. Open Grafana Dashboard
6. View K6 Results
7. Stop Prometheus Exporter
8. Exit
```

---

## 📈 Grafana Dashboard JSON

Save this as `k6-dashboard.json` in Grafana:

```json
{
  "dashboard": {
    "title": "K6 Performance Tests",
    "panels": [
      {
        "title": "Requests Per Minute",
        "targets": [
          {
            "expr": "rate(k6_http_requests_total[1m])"
          }
        ]
      },
      {
        "title": "Error Rate",
        "targets": [
          {
            "expr": "k6_http_request_errors"
          }
        ]
      },
      {
        "title": "Response Time (ms)",
        "targets": [
          {
            "expr": "k6_http_request_duration_ms"
          }
        ]
      }
    ]
  }
}
```

---

## ✅ Verification Checklist

- [ ] Docker services running (`docker ps`)
- [ ] K6 installed (`k6 version`)
- [ ] Node.js installed (`node --version`)
- [ ] Prometheus exporter running (port 6565)
- [ ] Prometheus scraping K6 (check targets)
- [ ] Grafana accessible (http://localhost:3001)
- [ ] K6 tests sending metrics to exporter
- [ ] Grafana dashboard showing K6 metrics

---

## 🔄 CI/CD Integration

Chạy trong GitHub Actions:

```yaml
- name: Run K6 Tests
  run: |
    node k6/scripts/k6-prometheus-server.js &
    sleep 5
    k6 run --out json=k6-results.json k6/tests/k6-prometheus.js
    
- name: Check Results
  run: |
    cat k6-results.json | jq '.metrics'
```

---

## 📞 Support Commands

```powershell
# View Prometheus metrics
curl http://localhost:6565/metrics | Select-String 'k6_'

# Test health
curl http://localhost:6565/health

# View K6 summary
cat k6-summary.json | ConvertFrom-Json

# Kill exporter
taskkill /IM node.exe /F

# Clean up all containers
docker-compose down -v

# View logs
docker-compose logs prometheus
docker-compose logs grafana
```

---

**Status:** ✅ Ready for K6 + Prometheus + Grafana Integration

**Next:** Run the interactive script or follow step-by-step guide above!
