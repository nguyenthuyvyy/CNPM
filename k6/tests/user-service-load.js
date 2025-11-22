import http from 'k6/http';
import { check, group, sleep } from 'k6';
import { Rate, Trend, Counter, Gauge } from 'k6/metrics';

// Custom metrics
const errorRate = new Rate('errors');
const successRate = new Rate('success');
const loginTrend = new Trend('login_duration');
const getUserTrend = new Trend('get_user_duration');
const createUserTrend = new Trend('create_user_duration');
const errorCount = new Counter('error_count');
const successCount = new Counter('success_count');

export const options = {
  stages: [
    { duration: '30s', target: 20 },   // Ramp up 20 users over 30s
    { duration: '1m30s', target: 50 }, // Ramp up to 50 users over 1m30s
    { duration: '2m', target: 50 },    // Stay at 50 users for 2m
    { duration: '30s', target: 0 },    // Ramp down 0 users over 30s
  ],
  thresholds: {
    http_req_duration: ['p(99)<1500', 'p(95)<1000'],
    http_req_failed: ['rate<0.1'],
    errors: ['rate<0.1'],
  },
  ext: {
    loadimpact: {
      projectID: 3486629,
      name: 'CNPM User Service - Load Test',
    },
  },
};

const BASE_URL = __ENV.BASE_URL || 'http://localhost:8081';

export default function () {
  group('User Service - Load Test', () => {
    // Test 1: Register User
    group('Register User', () => {
      const registerPayload = JSON.stringify({
        fullname: `User_${__VU}_${Date.now()}`,
        email: `user_${__VU}_${Date.now()}@example.com`,
        phone: '1234567890',
        password: 'SecurePass123!',
        role: 'CUSTOMER',
      });

      const registerRes = http.post(`${BASE_URL}/api/auth/register`, registerPayload, {
        headers: { 'Content-Type': 'application/json' },
      });

      const registerSuccess = check(registerRes, {
        'Register status is 200/201': (r) => r.status === 200 || r.status === 201,
        'Register response has token': (r) => r.body.includes('token'),
      });

      if (registerSuccess) {
        successCount.add(1);
        successRate.add(1);
      } else {
        errorCount.add(1);
        errorRate.add(1);
      }
    });

    sleep(1);

    // Test 2: Login User
    group('Login User', () => {
      const loginPayload = JSON.stringify({
        email: `user_${__VU}_${Date.now()}@example.com`,
        password: 'SecurePass123!',
      });

      const loginRes = http.post(`${BASE_URL}/api/auth/login`, loginPayload, {
        headers: { 'Content-Type': 'application/json' },
      });

      const loginSuccess = check(loginRes, {
        'Login status is 200': (r) => r.status === 200,
        'Login response has token': (r) => r.body.includes('token'),
      });

      loginTrend.add(loginRes.timings.duration);

      if (loginSuccess) {
        successCount.add(1);
        successRate.add(1);
      } else {
        errorCount.add(1);
        errorRate.add(1);
      }
    });

    sleep(1);

    // Test 3: Get All Users
    group('Get All Users', () => {
      const getAllRes = http.get(`${BASE_URL}/api/users`, {
        headers: { 'Content-Type': 'application/json' },
      });

      const getAllSuccess = check(getAllRes, {
        'Get all users status is 200': (r) => r.status === 200,
        'Get all users response is array': (r) => r.body.includes('['),
      });

      if (getAllSuccess) {
        successCount.add(1);
        successRate.add(1);
      } else {
        errorCount.add(1);
        errorRate.add(1);
      }
    });

    sleep(1);

    // Test 4: Get User by ID
    group('Get User by ID', () => {
      const getUserRes = http.get(`${BASE_URL}/api/users/1`, {
        headers: { 'Content-Type': 'application/json' },
      });

      const getUserSuccess = check(getUserRes, {
        'Get user by ID status is 200 or 404': (r) => r.status === 200 || r.status === 404,
      });

      getUserTrend.add(getUserRes.timings.duration);

      if (getUserSuccess) {
        successCount.add(1);
        successRate.add(1);
      } else {
        errorCount.add(1);
        errorRate.add(1);
      }
    });

    sleep(1);

    // Test 5: Create User (Admin)
    group('Create User', () => {
      const createUserPayload = JSON.stringify({
        fullname: `NewUser_${Date.now()}`,
        email: `newuser_${Date.now()}@example.com`,
        phone: '0987654321',
        password: 'NewPass123!',
        role: 'CUSTOMER',
      });

      const createRes = http.post(`${BASE_URL}/api/users`, createUserPayload, {
        headers: { 'Content-Type': 'application/json' },
      });

      const createSuccess = check(createRes, {
        'Create user status is 200/201': (r) => r.status === 200 || r.status === 201,
      });

      createUserTrend.add(createRes.timings.duration);

      if (createSuccess) {
        successCount.add(1);
        successRate.add(1);
      } else {
        errorCount.add(1);
        errorRate.add(1);
      }
    });

    sleep(2);
  });
}
