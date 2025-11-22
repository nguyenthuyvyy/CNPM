# K6 CI Integration Guide - Hiển thị Metrics trong Grafana

## 🎯 Mục tiêu
Chạy K6 load tests trong CI/CD pipeline và tự động hiển thị metrics trong Grafana Dashboard

## 📋 Kiến trúc

```
┌─────────────────┐
│   CI Pipeline   │
│  (GitHub Actions)
└────────┬────────┘
         │
         ├─ Run K6 Tests
         │  (Prometheus format output)
         │
         ├─ Push Metrics
         │  (localhost:6565)
         │
         └─ Trigger Grafana Update
            (http://localhost:3000)
```

## 🚀 Setup Steps

### Step 1: Chuẩn bị Prometheus cho K6

Prometheus config đã được cập nhật để scrape K6 metrics:

```yaml
# monitoring/prometheus.yml
- job_name: "k6"
  static_configs:
    - targets: ["localhost:6565"]
```

### Step 2: Tạo K6 Test với Prometheus Output

**File: `k6/tests/k6-prometheus.js`**

```javascript
import http from 'k6/http';
import { check, group, sleep } from 'k6';
import { Rate, Trend, Counter } from 'k6/metrics';

// Prometheus metrics
const errorRate = new Rate('k6_http_errors');
const requestDuration = new Trend('k6_http_duration_ms');
const requestCount = new Counter('k6_requests_total');
const activeVUs = new Gauge('k6_vus_active');

export const options = {
  stages: [
    { duration: '30s', target: 10 },  // Ramp-up
    { duration: '1m', target: 20 },   // Maintain
    { duration: '30s', target: 0 },   // Ramp-down
  ],
  thresholds: {
    'k6_http_errors': ['rate<0.1'],
    'k6_http_duration_ms': ['p(95)<1000', 'p(99)<2000'],
    'k6_requests_total': ['value>50'],
  },
};

const BASE_URL = __ENV.BASE_URL || 'http://localhost:8081';

export default function () {
  group('Performance Tests', () => {
    // Test Health Check
    const res = http.get(`${BASE_URL}/actuator/health`);
    
    activeVUs.set(__VU);
    requestCount.add(1);
    requestDuration.add(res.timings.duration);
    
    check(res, {
      'status 200': (r) => r.status === 200,
      'response time < 1s': (r) => r.timings.duration < 1000,
    });

    if (res.status !== 200) {
      errorRate.add(true);
    }
  });

  sleep(1);
}

// Export summary to stdout for parsing
export function handleSummary(data) {
  console.log('=== K6 Test Summary ===');
  console.log(`Total Requests: ${data.metrics.k6_requests_total?.value || 0}`);
  console.log(`Error Rate: ${(data.metrics.k6_http_errors?.value * 100).toFixed(2)}%`);
  console.log(`Avg Duration: ${data.metrics.k6_http_duration_ms?.value.toFixed(2)}ms`);
  return data;
}
```

### Step 3: Setup Local K6 Prometheus Exporter

**File: `k6/scripts/prometheus-server.js`** (Node.js)

```javascript
const http = require('http');
const fs = require('fs');

// In-memory metrics storage
const metrics = {
  'k6_requests_total': 0,
  'k6_http_errors': 0,
  'k6_http_duration_ms': 0,
  'k6_vus_active': 0,
};

// Prometheus format output
function formatMetrics() {
  let output = '';
  output += `# HELP k6_requests_total Total number of requests\n`;
  output += `# TYPE k6_requests_total counter\n`;
  output += `k6_requests_total ${metrics['k6_requests_total']}\n\n`;

  output += `# HELP k6_http_errors Rate of HTTP errors\n`;
  output += `# TYPE k6_http_errors gauge\n`;
  output += `k6_http_errors ${metrics['k6_http_errors']}\n\n`;

  output += `# HELP k6_http_duration_ms HTTP request duration in ms\n`;
  output += `# TYPE k6_http_duration_ms gauge\n`;
  output += `k6_http_duration_ms ${metrics['k6_http_duration_ms']}\n\n`;

  output += `# HELP k6_vus_active Active virtual users\n`;
  output += `# TYPE k6_vus_active gauge\n`;
  output += `k6_vus_active ${metrics['k6_vus_active']}\n`;

  return output;
}

// HTTP Server for Prometheus scraping
const server = http.createServer((req, res) => {
  if (req.url === '/metrics') {
    res.writeHead(200, { 'Content-Type': 'text/plain' });
    res.end(formatMetrics());
  } else if (req.url === '/health') {
    res.writeHead(200, { 'Content-Type': 'application/json' });
    res.end(JSON.stringify({ status: 'ok' }));
  } else {
    res.writeHead(404);
    res.end('Not Found');
  }
});

const PORT = process.env.PORT || 6565;
server.listen(PORT, '0.0.0.0', () => {
  console.log(`K6 Prometheus Exporter running on port ${PORT}`);
  console.log(`Metrics: http://localhost:${PORT}/metrics`);
});

// Graceful shutdown
process.on('SIGINT', () => {
  server.close(() => process.exit(0));
});

module.exports = { metrics };
```

### Step 4: GitHub Actions CI/CD Integration

**File: `.github/workflows/k6-performance-test.yml`**

```yaml
name: K6 Performance Tests

on:
  push:
    branches: [ main, develop ]
  pull_request:
    branches: [ main ]
  schedule:
    - cron: '0 2 * * *'  # Daily at 2 AM

jobs:
  k6-tests:
    runs-on: ubuntu-latest
    
    services:
      postgres:
        image: postgres:16
        env:
          POSTGRES_DB: foodfast_db
          POSTGRES_PASSWORD: postgres
        options: >-
          --health-cmd pg_isready
          --health-interval 10s
          --health-timeout 5s
          --health-retries 5

    steps:
      - uses: actions/checkout@v3
      
      - name: Setup Java
        uses: actions/setup-java@v3
        with:
          java-version: '21'
          distribution: 'temurin'
          cache: maven
      
      - name: Build Backend Services
        run: |
          for service in DoAnCNPM_Backend/*_service DoAnCNPM_Backend/eureka_server DoAnCNPM_Backend/api-gateway; do
            if [ -f "$service/pom.xml" ]; then
              cd "$service"
              mvn clean package -DskipTests
              cd ../../
            fi
          done
      
      - name: Start Services with Docker Compose
        run: |
          docker-compose up -d
          sleep 30  # Wait for services to start
      
      - name: Install K6
        run: |
          sudo apt-key adv --keyserver hkp://keyserver.ubuntu.com:80 --recv-keys C5AD17C747E3415A3642D57D77C6C491D6AC1D69
          echo "deb https://dl.k6.io/deb stable main" | sudo tee /etc/apt/sources.list.d/k6-stable.list
          sudo apt-get update
          sudo apt-get install -y k6
      
      - name: Start Prometheus Exporter
        run: |
          node k6/scripts/prometheus-server.js &
          sleep 5
      
      - name: Run K6 Tests
        env:
          BASE_URL: http://localhost:8081
        run: |
          k6 run k6/tests/k6-prometheus.js \
            --out json=k6-results.json \
            --summary-export=k6-summary.json
      
      - name: Check K6 Results
        run: |
          cat k6-summary.json | jq .
      
      - name: Generate Grafana Report
        if: always()
        run: |
          echo "K6 Performance Test Results" > k6-report.txt
          echo "=========================" >> k6-report.txt
          jq '.metrics | keys[]' k6-results.json >> k6-report.txt
      
      - name: Upload K6 Results
        if: always()
        uses: actions/upload-artifact@v3
        with:
          name: k6-results
          path: |
            k6-results.json
            k6-summary.json
            k6-report.txt
      
      - name: Stop Services
        if: always()
        run: docker-compose down
```

### Step 5: Local Testing Workflow

**1. Start Infrastructure:**
```bash
cd D:\cnpm\CNPM-3
docker-compose up -d
```

**2. Start Prometheus Exporter (PowerShell):**
```powershell
node k6/scripts/prometheus-server.js
# Output: K6 Prometheus Exporter running on port 6565
```

**3. Run K6 Tests (New Terminal):**
```bash
cd D:\cnpm\CNPM-3
k6 run k6/tests/k6-prometheus.js --env BASE_URL=http://localhost:8081
```

**4. View Metrics in Grafana:**
- Open http://localhost:3001
- Login: admin / 1admin1
- Create new dashboard or use existing
- Add Prometheus data source: http://prometheus:9090
- Create panels with queries:
  - `k6_requests_total`
  - `k6_http_errors`
  - `k6_http_duration_ms`
  - `k6_vus_active`

## 📊 Grafana Dashboard Setup

### Create K6 Performance Panel

**Panel 1: Request Rate**
```
Query: rate(k6_requests_total[1m])
Type: Graph
Title: Requests per Minute
```

**Panel 2: Error Rate**
```
Query: k6_http_errors
Type: Stat
Title: Error Rate
```

**Panel 3: Response Time**
```
Query: k6_http_duration_ms
Type: Graph
Title: Response Duration (ms)
```

**Panel 4: Active VUs**
```
Query: k6_vus_active
Type: Gauge
Title: Active Users
```

## 🔧 Configuration Files

### Update Prometheus Config

**File: `monitoring/prometheus.yml`**

```yaml
scrape_configs:
  # ... existing configs ...
  
  - job_name: "k6"
    static_configs:
      - targets: ["localhost:6565"]
    scrape_interval: 5s
    scrape_timeout: 3s
```

## 📈 Monitoring Thresholds

Configure thresholds trong K6 tests:

```javascript
thresholds: {
  'k6_http_errors': ['rate<0.05'],      // < 5% error rate
  'k6_http_duration_ms': ['p(95)<1000'], // 95% under 1s
  'k6_requests_total': ['value>100'],    // At least 100 requests
}
```

## 🚨 Troubleshooting

### K6 không kết nối Prometheus
```bash
# Check Prometheus exporter running
curl http://localhost:6565/metrics
```

### Metrics không hiển thị
```bash
# Check Prometheus targets
curl http://localhost:9090/api/v1/targets
```

### Docker services không start
```bash
# View logs
docker-compose logs -f

# Rebuild images
docker-compose down
mvn clean package -DskipTests
docker-compose up -d
```

## ✅ Verification Checklist

- [ ] Docker services running (`docker ps`)
- [ ] K6 installed (`k6 version`)
- [ ] Prometheus exporter running (port 6565)
- [ ] Prometheus scraping K6 (`http://localhost:9090/targets`)
- [ ] Grafana accessible (`http://localhost:3001`)
- [ ] K6 test metrics in Grafana dashboard

## 📝 Next Steps

1. **Create K6 test files** cho các services khác
2. **Setup automated dashboards** trong Grafana
3. **Configure alerts** khi metrics vượt ngưỡng
4. **Store historical data** để so sánh performance
5. **Integrate vào CI/CD** để chạy tự động

---

**Status**: Ready for K6 + Prometheus + Grafana integration ✅
