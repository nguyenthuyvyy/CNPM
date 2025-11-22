import http from 'k6/http';
import { check, group, sleep } from 'k6';
import { Rate, Trend, Counter, Gauge } from 'k6/metrics';

// Custom metrics for Prometheus
const errorRate = new Rate('k6_errors');
const successRate = new Rate('k6_success');
const requestDuration = new Trend('k6_request_duration');
const requestCount = new Counter('k6_requests_total');
const activeConnections = new Gauge('k6_active_connections');

export const options = {
  stages: [
    { duration: '1m', target: 10 },
    { duration: '2m', target: 20 },
    { duration: '1m', target: 0 },
  ],
  thresholds: {
    'k6_errors': ['rate<0.1'],
    'k6_request_duration': ['p(99)<2000', 'p(95)<1000'],
  },
};

const BASE_URL = __ENV.BASE_URL || 'http://localhost:8081';

export default function () {
  group('Health Check', () => {
    const res = http.get(`${BASE_URL}/actuator/health`);
    
    activeConnections.set(__VU);
    requestCount.add(1);
    
    if (res.status === 200) {
      successRate.add(true);
      requestDuration.add(res.timings.duration);
    } else {
      errorRate.add(true);
    }

    check(res, {
      'status is 200': (r) => r.status === 200,
    });
  });

  sleep(1);
}

export function handleSummary(data) {
  console.log('Metrics Summary:');
  console.log(JSON.stringify(data, null, 2));
  return data;
}
