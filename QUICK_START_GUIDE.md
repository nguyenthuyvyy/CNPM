# 🚀 CNPM FastFoodDrone - Quick Start Guide

**Last Updated:** November 23, 2025  
**System Status:** ✅ All Services Running

---

## 📋 Prerequisites

- Docker Desktop (or Docker + Docker Compose)
- Java 21 JDK
- Maven 3.8+
- Node.js 18+ (for K6 exporter)
- 4GB RAM minimum (8GB recommended)
- 20GB disk space

---

## ⚡ Quick Start (30 seconds)

### 1. Start All Services
```bash
cd CNPM-3
docker-compose up -d
```

### 2. Verify Services Running
```bash
docker-compose ps
```

Expected output: 14/14 containers UP

### 3. Check Health
```bash
curl http://localhost:8085/actuator/health
```

---

## 📊 Service Ports & Access

### Backend Services
| Service | Port | URL |
|---------|------|-----|
| **Eureka Server** | 8761 | http://localhost:8761 |
| **API Gateway** | 8085 | http://localhost:8085 |
| **User Service** | 8081 | http://localhost:8081 |
| **Product Service** | 8088 | http://localhost:8088 |
| **Order Service** | 8082 | http://localhost:8082 |
| **Payment Service** | 8084 | http://localhost:8084 |
| **Restaurant Service** | 8083 | http://localhost:8083 |
| **Drone Service** | 8089 | http://localhost:8089 |

### Databases
| Database | Port | Connection |
|----------|------|-----------|
| **PostgreSQL** | 5433 | `postgresql://postgres:1@localhost:5433/foodfast_db` |
| **MongoDB** | 27017 | `mongodb://admin:admin@localhost:27017` |

### Monitoring
| Service | Port | URL |
|---------|------|-----|
| **Prometheus** | 9090 | http://localhost:9090 |
| **Grafana** | 3001 | http://localhost:3001 |
| **K6 Exporter** | 6565 | http://localhost:6565/metrics |
| **Node-Exporter** | 9100 | http://localhost:9100 |

---

## 🧪 Running Performance Tests

### Run K6 Test (30 seconds, 10 VUs)
```bash
docker run --rm --network cnpm-3_foodfast-net \
  -v ${PWD}:/workspace \
  grafana/k6:latest \
  run --out json=/workspace/k6-results.json \
  -d 30s -u 10 \
  /workspace/k6/tests/working-k6-test.js
```

### Run Extended Test (1 minute, 20 VUs)
```bash
docker run --rm --network cnpm-3_foodfast-net \
  -v ${PWD}:/workspace \
  grafana/k6:latest \
  run --out json=/workspace/k6-results.json \
  -d 1m -u 20 \
  /workspace/k6/tests/working-k6-test.js
```

### Expected Results
- ✅ 1200+ requests completed
- ✅ 100% success rate
- ✅ p95 response time < 50ms
- ✅ 0 errors

---

## 🔍 Testing API Endpoints

### Through API Gateway (Recommended)
```bash
# List all users
curl http://localhost:8085/users

# Get user by ID
curl http://localhost:8085/users/1

# List products
curl http://localhost:8085/products

# List restaurants
curl http://localhost:8085/restaurants

# Get orders
curl http://localhost:8085/orders
```

### Direct Service Access
```bash
# User Service
curl http://localhost:8081/users

# Product Service
curl http://localhost:8088/products

# Order Service
curl http://localhost:8082/orders

# Payment Service
curl http://localhost:8084/payments

# Eureka Service Registry
curl http://localhost:8761/eureka/apps
```

---

## 📊 Monitoring & Dashboards

### Prometheus
- URL: http://localhost:9090
- Query metrics: `http_requests_total`, `http_request_duration_ms`, etc.
- Targets: http://localhost:9090/targets

### Grafana
- URL: http://localhost:3001
- Default login: `admin` / `admin`
- Add Prometheus datasource: `http://prometheus:9090`

### K6 Metrics
- URL: http://localhost:6565/metrics
- Format: Prometheus text format
- Updates: Every 1 second (file watching)

---

## 🛠️ Common Commands

### Check Service Status
```bash
# View all containers
docker-compose ps

# View specific service
docker-compose ps user-service

# View service logs (last 50 lines)
docker logs user-service -n 50

# Follow logs in real-time
docker logs -f user-service
```

### Stop/Start Services
```bash
# Start all services
docker-compose up -d

# Stop all services
docker-compose down

# Restart specific service
docker-compose restart user-service

# Rebuild and start
docker-compose up -d --build
```

### Database Operations
```bash
# Access PostgreSQL
docker exec -it postgres-db psql -U postgres -d foodfast_db

# Access MongoDB
docker exec -it mongodb mongo --username admin --password admin

# List MongoDB databases
mongo --username admin --password admin --eval "db.adminCommand('listDatabases')"
```

### View Container Logs
```bash
# API Gateway
docker logs api-gateway -f

# Eureka Server
docker logs eureka-server -f

# Prometheus
docker logs prometheus -f

# All services (follow)
docker-compose logs -f
```

---

## 🔧 Configuration Files

### Docker Compose
**File:** `docker-compose.yml`
- Defines all 14 services
- Network: `cnpm-3_foodfast-net`
- Volumes for persistence
- Environment variables

### Prometheus Config
**File:** `monitoring/prometheus.yml`
- Scrape targets
- K6 exporter (port 6565)
- Node exporter (port 9100)
- Scrape interval: 5 seconds

### K6 Exporter
**File:** `k6/scripts/k6-prometheus-server.js`
- Parses K6 results JSON
- Exposes metrics on port 6565
- Watches for file updates

### CI/CD Pipeline
**File:** `.github/workflows/ci-cd.yml`
- Test backend (all 8 services)
- Build Docker images
- Push to DockerHub
- K6 performance tests (new!)

---

## 🚨 Troubleshooting

### Container Won't Start
```bash
# Check logs
docker logs <service-name>

# Check resource usage
docker stats

# Remove and recreate
docker-compose down
docker-compose up -d --build
```

### Network Issues
```bash
# Inspect network
docker network inspect cnpm-3_foodfast-net

# Test connectivity between containers
docker exec <container1> ping <container2>
```

### Port Already in Use
```bash
# Find process using port
lsof -i :8085

# Kill process
kill -9 <PID>

# Or change port in docker-compose.yml
# Change: "8085:8085" to "8086:8085"
```

### Database Connection Errors
```bash
# Check PostgreSQL
docker exec postgres-db pg_isready

# Check MongoDB
docker exec mongodb mongo --eval "db.adminCommand('ping')"
```

### K6 Test Failures
```bash
# Check K6 exporter logs
docker logs k6-exporter

# Test metrics endpoint
curl http://localhost:6565/metrics

# Check K6 results file
cat k6-results.json | head -20
```

---

## 📈 Performance Tuning

### Increase Virtual Users
```bash
docker run --rm --network cnpm-3_foodfast-net \
  -v ${PWD}:/workspace \
  grafana/k6:latest \
  run -d 2m -u 50 \
  /workspace/k6/tests/working-k6-test.js
```

### Increase Test Duration
```bash
docker run --rm --network cnpm-3_foodfast-net \
  -v ${PWD}:/workspace \
  grafana/k6:latest \
  run -d 5m -u 20 \
  /workspace/k6/tests/working-k6-test.js
```

### Monitor Resource Usage
```bash
# CPU, Memory, Network in real-time
docker stats

# Detailed container info
docker inspect <container-name>
```

---

## 🔒 Security Considerations

### Database Access
- PostgreSQL user: `postgres`
- PostgreSQL password: `1` (change in production!)
- MongoDB user: `admin`
- MongoDB password: `admin` (change in production!)

### API Security
- API Gateway handles authentication
- Services communicate internally
- Use network isolation in production

### Monitoring
- Prometheus has no authentication (use reverse proxy)
- Grafana default: admin/admin (change password!)
- K6 metrics endpoint public (use firewall)

---

## 📝 Deployment to Production

### Before Deploying

1. **Change Passwords**
   - PostgreSQL
   - MongoDB
   - Grafana admin

2. **Configure Environment**
   - Update database URLs
   - Configure external services
   - Set up SSL certificates

3. **Scale Services**
   - Add replicas in docker-compose
   - Configure load balancer
   - Set resource limits

4. **Enable Monitoring**
   - Set up alerts in Grafana
   - Configure logging
   - Enable metrics collection

### Deploy Command
```bash
docker-compose -f docker-compose.yml \
  -f docker-compose.prod.yml \
  up -d
```

---

## 📊 Current Performance Baseline

Based on latest K6 test (20 VUs, 1 minute):

- **Throughput:** 20.6 req/s ✅
- **Error Rate:** 0% ✅
- **Response Time (avg):** 4.46 ms ✅
- **Response Time (p95):** 12.1 ms ✅
- **Check Pass Rate:** 100% ✅

---

## 🎯 Next Steps

1. **Access Grafana:** http://localhost:3001
   - Login: admin/admin
   - Add K6 dashboard

2. **Run Performance Tests:** Use K6 commands above
   - Monitor in Prometheus
   - Visualize in Grafana

3. **Review API Documentation:**
   - Swagger: http://localhost:8085/swagger-ui.html
   - OpenAPI: http://localhost:8085/api-docs

4. **Deploy Frontend:**
   - React web app: `DoAnCNPM_Frontend/web`
   - Mobile app: `DoAnCNPM_Frontend/mobile`

5. **Scale for Production:**
   - Configure database replication
   - Set up Kubernetes manifests
   - Enable auto-scaling

---

## 📞 Support

### Debug Endpoint
```bash
# Full system health check
curl -s http://localhost:8761/eureka/apps | jq '.'

# Prometheus targets
curl -s http://localhost:9090/api/v1/targets | jq '.data.activeTargets[]'

# K6 metrics
curl http://localhost:6565/metrics
```

### Common Issues

| Issue | Solution |
|-------|----------|
| Services not starting | Check Docker daemon, run `docker-compose logs` |
| Port already in use | Kill process or change port in docker-compose.yml |
| K6 test failures | Check network connectivity, verify K6 exporter running |
| Database errors | Verify PostgreSQL/MongoDB running, check credentials |
| Metrics not showing | Check Prometheus targets, verify scrape interval |

---

## ✅ Verification Checklist

- [ ] All 14 containers running: `docker-compose ps`
- [ ] Eureka has 8 services: http://localhost:8761
- [ ] API Gateway responding: `curl http://localhost:8085/actuator/health`
- [ ] K6 exporter working: `curl http://localhost:6565/metrics`
- [ ] Prometheus collecting: http://localhost:9090/targets
- [ ] Grafana accessible: http://localhost:3001
- [ ] K6 test passing: Run performance test
- [ ] Databases connected: PostgreSQL & MongoDB responding

---

## 📚 Documentation

- **System Architecture:** See `DEPLOYMENT_VERIFIED.md`
- **Status Report:** See `SYSTEM_STATUS_REPORT.md`
- **Backend Services:** See `README.md` in each service folder
- **Frontend:** See `DoAnCNPM_Frontend/README.md`
- **API Docs:** http://localhost:8085/swagger-ui.html (when running)

---

## 🎉 System Ready!

```
✅ All 14 containers running
✅ Services healthy and registered
✅ Databases connected
✅ Monitoring operational
✅ Performance tests passing
✅ Ready for use!
```

**Happy deploying! 🚀**

For issues: Check logs with `docker logs <service-name>`  
For metrics: Visit http://localhost:9090  
For dashboards: Visit http://localhost:3001
