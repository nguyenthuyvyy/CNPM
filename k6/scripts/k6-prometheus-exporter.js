#!/usr/bin/env node

/**
 * K6 Prometheus Exporter
 * Runs K6 tests and exposes metrics to Prometheus format on port 6565
 */

const http = require('http');
const { exec } = require('child_process');
const path = require('path');

// Metrics storage
let metrics = {
  k6_errors: { type: 'counter', value: 0 },
  k6_success: { type: 'counter', value: 0 },
  k6_request_duration: { type: 'gauge', value: 0 },
  k6_requests_total: { type: 'counter', value: 0 },
  k6_active_connections: { type: 'gauge', value: 0 },
  k6_vus: { type: 'gauge', value: 0 },
  k6_test_duration: { type: 'gauge', value: 0 },
};

// Helper to format metrics in Prometheus format
function formatPrometheusMetrics() {
  let output = '';
  
  for (const [name, data] of Object.entries(metrics)) {
    output += `# TYPE ${name} ${data.type}\n`;
    output += `${name} ${data.value}\n`;
  }
  
  return output;
}

// Create HTTP server for Prometheus scraping
const server = http.createServer((req, res) => {
  if (req.url === '/metrics') {
    res.writeHead(200, { 'Content-Type': 'text/plain' });
    res.end(formatPrometheusMetrics());
  } else if (req.url === '/health') {
    res.writeHead(200, { 'Content-Type': 'application/json' });
    res.end(JSON.stringify({ status: 'ok' }));
  } else {
    res.writeHead(404);
    res.end('Not Found');
  }
});

const PORT = process.env.PROMETHEUS_PORT || 6565;

server.listen(PORT, '0.0.0.0', () => {
  console.log(`K6 Prometheus Exporter listening on port ${PORT}`);
  console.log(`Metrics available at http://0.0.0.0:${PORT}/metrics`);
});

// Function to update metrics from K6 output
function parseK6Output(output) {
  try {
    const lines = output.split('\n');
    lines.forEach(line => {
      // Parse metrics from K6 output
      if (line.includes('errors') && line.includes('rate')) {
        const match = line.match(/(\d+\.?\d*)/);
        if (match) metrics.k6_errors.value = parseFloat(match[0]);
      }
      if (line.includes('requests') && line.includes('total')) {
        const match = line.match(/(\d+)/);
        if (match) metrics.k6_requests_total.value = parseInt(match[0]);
      }
    });
  } catch (e) {
    console.error('Error parsing K6 output:', e);
  }
}

// Export metrics update function for external scripts
module.exports = {
  updateMetric: (name, value) => {
    if (metrics[name]) {
      metrics[name].value = value;
    }
  },
  getMetrics: () => metrics,
  formatPrometheusMetrics: formatPrometheusMetrics,
};

// Graceful shutdown
process.on('SIGINT', () => {
  console.log('\nShutting down K6 Prometheus Exporter...');
  server.close(() => {
    process.exit(0);
  });
});
