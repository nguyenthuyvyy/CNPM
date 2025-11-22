#!/bin/bash

# Import Grafana Dashboard Script
# This script imports the CNPM dashboard into Grafana

GRAFANA_URL="http://localhost:3001"
GRAFANA_ADMIN_USER="admin"
GRAFANA_ADMIN_PASSWORD="admin"
DASHBOARD_FILE="$(dirname "$0")/grafana-dashboard.json"

echo "🔄 Importing CNPM Dashboard to Grafana..."

# First, ensure Prometheus datasource exists
echo "✅ Setting up Prometheus datasource..."
curl -X POST "$GRAFANA_URL/api/datasources" \
  -H "Content-Type: application/json" \
  -u "$GRAFANA_ADMIN_USER:$GRAFANA_ADMIN_PASSWORD" \
  -d '{
    "name": "Prometheus",
    "type": "prometheus",
    "access": "proxy",
    "url": "http://prometheus:9090",
    "isDefault": true,
    "jsonData": {}
  }' 2>/dev/null || echo "⚠️  Datasource may already exist"

# Import dashboard
echo "📊 Importing dashboard..."
curl -X POST "$GRAFANA_URL/api/dashboards/db" \
  -H "Content-Type: application/json" \
  -u "$GRAFANA_ADMIN_USER:$GRAFANA_ADMIN_PASSWORD" \
  -d @"$DASHBOARD_FILE"

echo "✅ Dashboard imported successfully!"
echo "🌐 Access Grafana at: $GRAFANA_URL"
