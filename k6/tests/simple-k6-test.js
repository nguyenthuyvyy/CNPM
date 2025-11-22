import http from 'k6/http';
import { check, sleep } from 'k6';

export const options = {
  stages: [
    { duration: '10s', target: 5 },
    { duration: '20s', target: 10 },
    { duration: '10s', target: 0 },
  ],
  thresholds: {
    http_req_duration: ['p(95)<1000'],
    http_req_failed: ['rate<0.1'],
  },
};

export default function () {
  // Test API Gateway health
  let res = http.get('http://api-gateway:8085/actuator/health');
  check(res, {
    'health status ok': (r) => r.status === 200,
  });

  sleep(1);

  // Test get all users
  res = http.get('http://api-gateway:8085/api/users');
  check(res, {
    'users GET status ok': (r) => r.status === 200,
  });

  sleep(1);

  // Test get all products
  res = http.get('http://api-gateway:8085/api/products');
  check(res, {
    'products GET status ok': (r) => r.status === 200,
  });

  sleep(1);
}
