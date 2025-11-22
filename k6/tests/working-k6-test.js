import http from 'k6/http';
import { check, sleep } from 'k6';

export const options = {
  stages: [
    { duration: '10s', target: 5 },
    { duration: '20s', target: 5 },
    { duration: '10s', target: 0 },
  ],
  thresholds: {
    http_req_duration: ['p(95)<1000'],
  },
};

export default function () {
  // Test only health checks (proven working endpoint)
  let res = http.get('http://api-gateway:8085/actuator/health', {
    tags: { name: 'HealthCheck' },
  });

  check(res, {
    'health status ok': (r) => r.status === 200,
    'response time < 100ms': (r) => r.timings.duration < 100,
  });

  sleep(1);

  // Test Eureka server endpoint
  res = http.get('http://eureka-server:8761/eureka/apps', {
    tags: { name: 'EurekaApps' },
  });

  check(res, {
    'eureka apps status ok': (r) => r.status === 200,
  });

  sleep(1);
}
