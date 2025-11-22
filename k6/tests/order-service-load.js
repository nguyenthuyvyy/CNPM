import http from 'k6/http';
import { check, group, sleep } from 'k6';
import { Trend, Counter, Rate } from 'k6/metrics';

// Custom metrics
const orderCreateDuration = new Trend('order_create_duration');
const orderListDuration = new Trend('order_list_duration');
const orderDetailDuration = new Trend('order_detail_duration');
const errorCount = new Counter('error_count');
const successCount = new Counter('success_count');
const errorRate = new Rate('errors');

export const options = {
  stages: [
    { duration: '30s', target: 25 },   // Ramp up 25 users
    { duration: '2m', target: 40 },    // Ramp up to 40 users
    { duration: '2m', target: 40 },    // Stay at 40 users
    { duration: '30s', target: 0 },    // Ramp down
  ],
  thresholds: {
    http_req_duration: ['p(99)<3000', 'p(95)<2000'],
    http_req_failed: ['rate<0.2'],
    errors: ['rate<0.2'],
  },
};

const BASE_URL = __ENV.BASE_URL || 'http://localhost:8082';

export default function () {
  group('Order Service - Transaction Load Test', () => {
    // Test 1: Create Order
    group('Create Order', () => {
      const orderPayload = JSON.stringify({
        customerId: __VU,
        restaurantId: Math.floor(Math.random() * 5) + 1,
        items: [
          {
            productId: Math.floor(Math.random() * 10) + 1,
            quantity: Math.floor(Math.random() * 5) + 1,
            price: Math.floor(Math.random() * 200000) + 50000,
          },
        ],
        totalPrice: Math.floor(Math.random() * 500000) + 100000,
        status: 'PENDING',
      });

      const createRes = http.post(`${BASE_URL}/api/orders`, orderPayload, {
        headers: { 'Content-Type': 'application/json' },
      });

      const success = check(createRes, {
        'Create order status is 200/201': (r) => r.status === 200 || r.status === 201,
      });

      orderCreateDuration.add(createRes.timings.duration);

      if (success) {
        successCount.add(1);
      } else {
        errorCount.add(1);
        errorRate.add(1);
      }
    });

    sleep(1);

    // Test 2: Get all orders
    group('Get All Orders', () => {
      const listRes = http.get(`${BASE_URL}/api/orders`, {
        headers: { 'Content-Type': 'application/json' },
      });

      const success = check(listRes, {
        'Get orders status is 200': (r) => r.status === 200,
      });

      orderListDuration.add(listRes.timings.duration);

      if (success) {
        successCount.add(1);
      } else {
        errorCount.add(1);
        errorRate.add(1);
      }
    });

    sleep(1);

    // Test 3: Get order by ID
    group('Get Order by ID', () => {
      const orderId = Math.floor(Math.random() * 100) + 1;

      const detailRes = http.get(`${BASE_URL}/api/orders/${orderId}`, {
        headers: { 'Content-Type': 'application/json' },
      });

      const success = check(detailRes, {
        'Get order status is 200 or 404': (r) => r.status === 200 || r.status === 404,
      });

      orderDetailDuration.add(detailRes.timings.duration);

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
