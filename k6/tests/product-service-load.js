import http from 'k6/http';
import { check, group, sleep } from 'k6';
import { Trend, Counter, Rate } from 'k6/metrics';

// Custom metrics
const productListDuration = new Trend('product_list_duration');
const productDetailDuration = new Trend('product_detail_duration');
const errorCount = new Counter('error_count');
const successCount = new Counter('success_count');
const errorRate = new Rate('errors');

export const options = {
  stages: [
    { duration: '30s', target: 30 },   // Ramp up 30 users
    { duration: '2m', target: 50 },    // Ramp up to 50 users
    { duration: '2m', target: 50 },    // Stay at 50 users
    { duration: '30s', target: 0 },    // Ramp down
  ],
  thresholds: {
    http_req_duration: ['p(99)<2000', 'p(95)<1200'],
    http_req_failed: ['rate<0.15'],
    errors: ['rate<0.15'],
  },
};

const BASE_URL = __ENV.BASE_URL || 'http://localhost:8085';

export default function () {
  group('Product Service - Catalog Load Test', () => {
    // Test 1: Get all products
    group('Get All Products', () => {
      const listRes = http.get(`${BASE_URL}/api/products`, {
        headers: { 'Content-Type': 'application/json' },
      });

      const success = check(listRes, {
        'List products status is 200': (r) => r.status === 200,
        'Response has products': (r) => r.body.includes('[') || r.body.includes('data'),
      });

      productListDuration.add(listRes.timings.duration);

      if (success) {
        successCount.add(1);
      } else {
        errorCount.add(1);
        errorRate.add(1);
      }
    });

    sleep(1);

    // Test 2: Get product by ID
    group('Get Product by ID', () => {
      const productIds = [1, 2, 3, 4, 5];
      const randomId = productIds[Math.floor(Math.random() * productIds.length)];

      const detailRes = http.get(`${BASE_URL}/api/products/${randomId}`, {
        headers: { 'Content-Type': 'application/json' },
      });

      const success = check(detailRes, {
        'Get product status is 200 or 404': (r) => r.status === 200 || r.status === 404,
      });

      productDetailDuration.add(detailRes.timings.duration);

      if (success) {
        successCount.add(1);
      } else {
        errorCount.add(1);
        errorRate.add(1);
      }
    });

    sleep(1);

    // Test 3: Search products
    group('Search Products', () => {
      const searchRes = http.get(`${BASE_URL}/api/products?search=pizza`, {
        headers: { 'Content-Type': 'application/json' },
      });

      const success = check(searchRes, {
        'Search status is 200': (r) => r.status === 200,
      });

      if (success) {
        successCount.add(1);
      } else {
        errorCount.add(1);
        errorRate.add(1);
      }
    });

    sleep(2);
  });
}
