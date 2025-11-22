#!/usr/bin/env node

/**
 * K6 Prometheus Exporter Server
 * 
 * Exposes K6 metrics in Prometheus format
 * Usage: node k6-prometheus-server.js [PORT]
 */

const http = require('http');
const fs = require('fs');
const path = require('path');

// Metrics Storage
const metrics = {
  k6_http_requests_total: { type: 'counter', value: 0, help: 'Total HTTP requests' },
  k6_http_request_errors: { type: 'gauge', value: 0, help: 'HTTP request errors' },
  k6_http_request_duration_ms: { type: 'gauge', value: 0, help: 'HTTP request duration in ms' },
  k6_vus: { type: 'gauge', value: 0, help: 'Active virtual users' },
  k6_checks_passed: { type: 'counter', value: 0, help: 'Passed checks' },
  k6_checks_failed: { type: 'counter', value: 0, help: 'Failed checks' },
};

/**
 * Format metrics in Prometheus text format
 */
function formatPrometheusMetrics() {
  let output = '';
  
  for (const [name, meta] of Object.entries(metrics)) {
    output += `# HELP ${name} ${meta.help}\n`;
    output += `# TYPE ${name} ${meta.type}\n`;
    output += `${name} ${meta.value}\n`;
  }
  
  return output;
}

/**
 * Parse K6 JSON output and update metrics (handles NDJSON format)
 */
function parseK6Metrics(jsonData) {
  try {
    const lines = jsonData.trim().split('\n');
    const metricValues = {};
    
    // Process each NDJSON line and keep the latest value for each metric
    for (const line of lines) {
      if (!line.trim()) continue;
      
      try {
        const entry = JSON.parse(line);
        
        // Collect latest metric values
        if (entry.type === 'Point' && entry.data && entry.data.value !== undefined) {
          const metric = entry.metric;
          const value = entry.data.value;
          
          // Store the latest value for each metric
          if (!metricValues[metric]) {
            metricValues[metric] = value;
          } else {
            metricValues[metric] = value;  // Always take the latest
          }
        }
      } catch (e) {
        // Skip malformed JSON lines
      }
    }
    
    // Update metrics from collected values
    metrics.k6_http_requests_total.value = Math.round(metricValues.http_reqs || 0);
    metrics.k6_http_request_errors.value = Math.round(metricValues.http_req_failed || 0);
    metrics.k6_http_request_duration_ms.value = parseFloat((metricValues.http_req_duration || 0).toFixed(2));
    metrics.k6_vus.value = Math.round(metricValues.vus_max || metricValues.vus || 0);
    metrics.k6_checks_passed.value = Math.round(metricValues.checks || 0);
    metrics.k6_checks_failed.value = 0;  // Will be calculated if available
    
  } catch (error) {
    console.error('Error parsing K6 metrics:', error.message);
  }
}

/**
 * Update metrics from file
 */
function watchMetricsFile(filePath) {
  fs.watchFile(filePath, { persistent: true, interval: 1000 }, () => {
    try {
      const content = fs.readFileSync(filePath, 'utf-8');
      parseK6Metrics(content);
      console.log(`📊 Metrics updated from ${path.basename(filePath)}`);
    } catch (error) {
      console.error(`Error reading metrics file: ${error.message}`);
    }
  });
}

/**
 * Create HTTP server
 */
function createServer() {
  return http.createServer((req, res) => {
    // Enable CORS
    res.setHeader('Access-Control-Allow-Origin', '*');
    res.setHeader('Access-Control-Allow-Methods', 'GET, OPTIONS');
    res.setHeader('Access-Control-Allow-Headers', 'Content-Type');

    if (req.method === 'OPTIONS') {
      res.writeHead(200);
      res.end();
      return;
    }

    if (req.url === '/metrics') {
      res.writeHead(200, { 'Content-Type': 'text/plain; version=0.0.4' });
      res.end(formatPrometheusMetrics());
      
    } else if (req.url === '/health') {
      res.writeHead(200, { 'Content-Type': 'application/json' });
      res.end(JSON.stringify({ 
        status: 'ok',
        timestamp: new Date().toISOString(),
        metrics_updated: new Date().toISOString(),
      }));
      
    } else if (req.url === '/') {
      res.writeHead(200, { 'Content-Type': 'text/html' });
      res.end(`
        <!DOCTYPE html>
        <html>
          <head>
            <title>K6 Prometheus Exporter</title>
            <style>
              body { font-family: Arial; margin: 20px; }
              h1 { color: #333; }
              a { color: #0066cc; text-decoration: none; margin-right: 20px; }
              a:hover { text-decoration: underline; }
              pre { background: #f0f0f0; padding: 10px; overflow-x: auto; }
            </style>
          </head>
          <body>
            <h1>🎯 K6 Prometheus Exporter</h1>
            <p>Running on port ${PORT}</p>
            <h2>Endpoints:</h2>
            <ul>
              <li><a href="/metrics">/metrics</a> - Prometheus metrics</li>
              <li><a href="/health">/health</a> - Health check</li>
              <li><a href="/">/</a> - This page</li>
            </ul>
            <h2>Usage:</h2>
            <pre>
# Run K6 test with JSON output
k6 run --out json=k6-results.json test.js

# View current metrics
curl http://localhost:${PORT}/metrics
            </pre>
          </body>
        </html>
      `);
      
    } else {
      res.writeHead(404, { 'Content-Type': 'application/json' });
      res.end(JSON.stringify({ error: 'Not Found' }));
    }
  });
}

// === Main ===
const PORT = process.env.PORT || process.argv[2] || 6565;
const METRICS_FILE = process.env.K6_METRICS || '/app/k6-results.json';

const server = createServer();

server.listen(PORT, '0.0.0.0', () => {
  console.log(`\n🚀 K6 Prometheus Exporter started`);
  console.log(`   Port: ${PORT}`);
  console.log(`   Metrics: http://localhost:${PORT}/metrics`);
  console.log(`   Health: http://localhost:${PORT}/health`);
  console.log(`   UI: http://localhost:${PORT}/`);
  console.log(`\n📁 Watching for metrics in: ${METRICS_FILE}`);
  console.log(`\n💡 Tip: Run K6 with --out json=k6-results.json`);
  console.log(`   k6 run --out json=k6-results.json k6/tests/k6-prometheus.js\n`);
});

// Watch for metrics file updates
if (fs.existsSync(METRICS_FILE)) {
  watchMetricsFile(METRICS_FILE);
  // Initial parse on startup
  try {
    const content = fs.readFileSync(METRICS_FILE, 'utf-8');
    parseK6Metrics(content);
    console.log(`📊 Initial metrics loaded from ${METRICS_FILE}`);
  } catch (e) {
    console.warn(`⚠️  Could not load initial metrics: ${e.message}`);
  }
} else {
  console.warn(`⚠️  Metrics file not found: ${METRICS_FILE}`);
  console.log('💡 Waiting for K6 test to create it...');
}

// Graceful shutdown
process.on('SIGINT', () => {
  console.log('\n\n👋 Shutting down K6 Prometheus Exporter...');
  server.close(() => {
    console.log('✅ Server closed');
    process.exit(0);
  });
});

process.on('SIGTERM', () => {
  process.exit(0);
});

// Handle uncaught errors
process.on('uncaughtException', (error) => {
  console.error('Uncaught Error:', error);
  process.exit(1);
});
