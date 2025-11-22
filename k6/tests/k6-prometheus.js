import http from 'k6/http';
import { check, group, sleep } from 'k6';
import { Rate, Trend, Counter, Gauge } from 'k6/metrics';

// === Prometheus Metrics ===
const httpErrorRate = new Rate('k6_http_request_errors');
const httpDuration = new Trend('k6_http_request_duration_ms');
const httpRequests = new Counter('k6_http_requests_total');
const activeVUs = new Gauge('k6_vus');

// === Test Configuration ===
export const options = {
  stages: [
    { duration: '30s', target: 5 },   // Ramp-up: 0 to 5 users
    { duration: '1m30s', target: 20 }, // Ramp-up: 5 to 20 users
    { duration: '2m', target: 20 },   // Stay at 20 users
    { duration: '30s', target: 0 },   // Ramp-down: 20 to 0 users
  ],
  thresholds: {
    'k6_http_request_errors': ['rate<0.1'],
    'k6_http_request_duration_ms': ['p(95)<1000', 'p(99)<2000'],
  },
};

const BASE_URL = __ENV.BASE_URL || 'http://localhost:8081';

// === Test Functions ===

function testHealthCheck() {
  group('Health Check', () => {
    const res = http.get(`${BASE_URL}/actuator/health`);
    httpRequests.add(1);
    httpDuration.add(res.timings.duration);
    
    if (res.status !== 200) {
      httpErrorRate.add(true);
    }
    
    check(res, {
      'health check passed': (r) => r.status === 200,
      'response time < 500ms': (r) => r.timings.duration < 500,
    });
  });
}

function testGetAllUsers() {
  group('Get All Users', () => {
    const res = http.get(`${BASE_URL}/api/users`);
    httpRequests.add(1);
    httpDuration.add(res.timings.duration);
    
    if (res.status !== 200) {
      httpErrorRate.add(true);
    }
    
    check(res, {
      'status 200': (r) => r.status === 200,
      'has users': (r) => r.body.length > 0,
    });
  });
}

function testCreateUser() {
  group('Create User', () => {
    const payload = JSON.stringify({
      fullname: `User_${Date.now()}`,
      email: `test_${Date.now()}@example.com`,
      password: 'Test@123456',
      phone: '0123456789',
    });

    const res = http.post(`${BASE_URL}/api/users`, payload, {
      headers: { 'Content-Type': 'application/json' },
    });

    httpRequests.add(1);
    httpDuration.add(res.timings.duration);

    if (res.status !== 201 && res.status !== 200) {
      httpErrorRate.add(true);
    }

    check(res, {
      'created successfully': (r) => r.status === 201 || r.status === 200,
      'response time < 1s': (r) => r.timings.duration < 1000,
    });
  });
}

function testUserLogin() {
  group('User Login', () => {
    const payload = JSON.stringify({
      email: 'test@example.com',
      password: 'Test@123456',
    });

    const res = http.post(`${BASE_URL}/api/auth/login`, payload, {
      headers: { 'Content-Type': 'application/json' },
    });

    httpRequests.add(1);
    httpDuration.add(res.timings.duration);

    if (res.status !== 200) {
      httpErrorRate.add(true);
    }

    check(res, {
      'login successful': (r) => r.status === 200 || r.status === 401,
      'response time < 1s': (r) => r.timings.duration < 1000,
    });
  });
}

// === Main Test Function ===
export default function () {
  activeVUs.set(__VU); // Update active VU count
  
  // Mix of test scenarios
  const testCase = Math.random();
  
  if (testCase < 0.2) {
    testHealthCheck();
  } else if (testCase < 0.4) {
    testGetAllUsers();
  } else if (testCase < 0.7) {
    testCreateUser();
  } else {
    testUserLogin();
  }

  sleep(1); // Wait 1 second between requests
}

// === Summary Export ===
export function handleSummary(data) {
  const summary = {
    timestamp: new Date().toISOString(),
    metrics: {
      total_requests: (data.metrics && data.metrics.k6_http_requests_total) ? data.metrics.k6_http_requests_total.value || 0 : 0,
      error_rate: (data.metrics && data.metrics.k6_http_request_errors) ? ((data.metrics.k6_http_request_errors.value || 0) * 100).toFixed(2) + '%' : '0%',
      avg_duration: (data.metrics && data.metrics.k6_http_request_duration_ms) ? (data.metrics.k6_http_request_duration_ms.value || 0).toFixed(2) + 'ms' : '0ms',
      p95_duration: (data.metrics && data.metrics.k6_http_request_duration_ms && data.metrics.k6_http_request_duration_ms.values) ? (data.metrics.k6_http_request_duration_ms.values.p95 || 0).toFixed(2) + 'ms' : '0ms',
      p99_duration: (data.metrics && data.metrics.k6_http_request_duration_ms && data.metrics.k6_http_request_duration_ms.values) ? (data.metrics.k6_http_request_duration_ms.values.p99 || 0).toFixed(2) + 'ms' : '0ms',
      max_vus: data.metrics && data.metrics.k6_vus && data.metrics.k6_vus.values ? Math.max(...data.metrics.k6_vus.values) : 0,
    },
  };

  console.log('='.repeat(60));
  console.log('🎯 K6 Performance Test Summary');
  console.log('='.repeat(60));
  console.log('Timestamp: ' + summary.timestamp);
  console.log('Total Requests: ' + summary.metrics.total_requests);
  console.log('Error Rate: ' + summary.metrics.error_rate);
  console.log('Avg Duration: ' + summary.metrics.avg_duration);
  console.log('P95 Duration: ' + summary.metrics.p95_duration);
  console.log('P99 Duration: ' + summary.metrics.p99_duration);
  console.log('Peak VUs: ' + summary.metrics.max_vus);
  console.log('='.repeat(60));

  return {
    'summary.json': JSON.stringify(summary, null, 2),
    stdout: console.log,
  };
}
