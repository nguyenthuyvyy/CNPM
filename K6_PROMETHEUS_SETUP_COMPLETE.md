# ✅ K6 + Prometheus + Grafana Integration Complete

## 📦 What's Been Setup

### 1. **K6 Test Files** (`k6/tests/`)
- ✅ `k6-prometheus.js` - Main test with Prometheus metrics
- ✅ `user-service-load.js` - User service load tests
- ✅ `product-service-load.js` - Product service load tests
- ✅ `order-service-load.js` - Order service load tests

### 2. **Prometheus Exporter** (`k6/scripts/`)
- ✅ `k6-prometheus-server.js` - Node.js server exposing metrics on port 6565
- ✅ `run-k6-test.bat` - Interactive menu script (Windows)
- ✅ `run-tests.sh` - Test runner (Linux/Mac)

### 3. **Configuration**
- ✅ `monitoring/prometheus.yml` - Updated with K6 job
- ✅ `docker-compose.yml` - Cleaned up (removed InfluxDB)

### 4. **Documentation**
- ✅ `k6/SETUP_GUIDE.md` - Complete setup guide
- ✅ `k6/CI_INTEGRATION_GUIDE.md` - CI/CD integration guide
- ✅ `k6/README.md` - General K6 documentation
- ✅ `k6/QUICK_START.md` - Quick start guide

---

## 🚀 Quick Start (3 Steps)

### Step 1: Start Infrastructure
```powershell
cd D:\cnpm\CNPM-3
docker-compose up -d
```

### Step 2: Start Prometheus Exporter
```powershell
node k6/scripts/k6-prometheus-server.js
# Running on http://localhost:6565
```

### Step 3: Run K6 Tests
```powershell
k6 run --out json=k6-results.json k6/tests/k6-prometheus.js
```

### View Results
- **Prometheus**: http://localhost:9090
- **Grafana**: http://localhost:3001

---

## 🎯 How It Works

```
┌──────────────────────────────────────────────────────────────┐
│                    K6 Tests (k6 run)                         │
│                  ├─ Health checks                            │
│                  ├─ User registration                        │
│                  ├─ Product queries                          │
│                  └─ Order operations                         │
└────────────────────────┬─────────────────────────────────────┘
                         │ JSON output (k6-results.json)
                         ▼
┌──────────────────────────────────────────────────────────────┐
│        Prometheus Exporter (port 6565)                       │
│      Watches k6-results.json for metrics:                    │
│        ├─ k6_http_requests_total (counter)                  │
│        ├─ k6_http_request_errors (gauge)                    │
│        ├─ k6_http_request_duration_ms (gauge)               │
│        ├─ k6_vus (gauge - active users)                     │
│        └─ k6_checks_passed/failed (counters)                │
└────────────────────────┬─────────────────────────────────────┘
                         │ /metrics endpoint
                         ▼
┌──────────────────────────────────────────────────────────────┐
│              Prometheus (port 9090)                          │
│        Scrapes metrics every 5 seconds                       │
│        Stores time-series data                              │
└────────────────────────┬─────────────────────────────────────┘
                         │ Query API
                         ▼
┌──────────────────────────────────────────────────────────────┐
│              Grafana (port 3001)                             │
│     Visualizes K6 metrics in real-time dashboard            │
│     Queries: rate(k6_http_requests_total[1m])               │
│            k6_http_request_duration_ms                       │
│            k6_http_request_errors                            │
└──────────────────────────────────────────────────────────────┘
```

---

## 📊 Prometheus Query Examples

**Get these metrics from Prometheus/Grafana:**

```promql
# Request rate (per minute)
rate(k6_http_requests_total[1m])

# Error rate (percentage)
(k6_http_request_errors / k6_http_requests_total) * 100

# Average response time
avg(k6_http_request_duration_ms)

# 95th percentile response time
histogram_quantile(0.95, k6_http_request_duration_ms)

# Active virtual users
k6_vus

# Total requests
k6_http_requests_total
```

---

## 🛠️ File Structure

```
k6/
├── SETUP_GUIDE.md                           ← Start here!
├── CI_INTEGRATION_GUIDE.md
├── QUICK_START.md
├── README.md
├── tests/
│   ├── k6-prometheus.js                    ← Main test file
│   ├── user-service-load.js
│   ├── product-service-load.js
│   └── order-service-load.js
├── scripts/
│   ├── k6-prometheus-server.js             ← Prometheus exporter
│   ├── run-k6-test.bat                     ← Windows menu script
│   ├── run-tests.bat
│   ├── run-tests.sh
│   └── prometheus-exporter.js
└── grafana/
    └── provisioning/
        ├── datasources/
        └── dashboards/

monitoring/
└── prometheus.yml                           ← K6 job added
```

---

## ✨ Key Features

✅ **Real-time monitoring** - K6 metrics update every 5 seconds
✅ **Integrated with Prometheus** - Reuse existing monitoring stack
✅ **Grafana dashboards** - Visualize all metrics together
✅ **CI/CD ready** - Easy to integrate into pipelines
✅ **No InfluxDB** - Single Prometheus source of truth
✅ **Windows/Linux/Mac** - Cross-platform support

---

## 🔧 Troubleshooting

### Exporter not starting?
```powershell
# Check Node.js installed
node --version

# Try with explicit port
node k6/scripts/k6-prometheus-server.js 6565
```

### K6 tests failing?
```powershell
# Check if services are running
curl http://localhost:8081/actuator/health

# Run with specific base URL
k6 run -e BASE_URL=http://localhost:8081 k6/tests/k6-prometheus.js
```

### Metrics not showing in Prometheus?
```powershell
# Check exporter is running
curl http://localhost:6565/metrics

# Restart Prometheus
docker-compose restart prometheus

# Check Prometheus targets: http://localhost:9090/targets
```

---

## 📈 Next Steps

1. ✅ Run the setup guide: `k6/SETUP_GUIDE.md`
2. ✅ Start infrastructure: `docker-compose up -d`
3. ✅ Start exporter: `node k6/scripts/k6-prometheus-server.js`
4. ✅ Run tests: `k6 run k6/tests/k6-prometheus.js`
5. ✅ View in Grafana: http://localhost:3001

---

## 🎓 Learning Resources

- **K6 Docs**: https://k6.io/docs/
- **Prometheus**: https://prometheus.io/docs/
- **Grafana**: https://grafana.com/docs/
- **Guide**: See `k6/SETUP_GUIDE.md` for detailed walkthrough

---

## 📞 Commands Reference

```powershell
# Start all services
docker-compose up -d

# Start K6 exporter
node k6/scripts/k6-prometheus-server.js

# Run K6 tests
k6 run --out json=k6-results.json k6/tests/k6-prometheus.js

# View metrics
curl http://localhost:6565/metrics

# View Prometheus
http://localhost:9090

# View Grafana
http://localhost:3001

# Stop all
docker-compose down
```

---

**Status**: ✅ Complete and Ready to Use!

**Next Action**: Follow the guide in `k6/SETUP_GUIDE.md`
