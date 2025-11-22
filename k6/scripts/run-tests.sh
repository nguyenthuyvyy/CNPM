#!/bin/bash

# CNPM K6 Load Test Runner with Dashboard
# This script runs k6 performance tests and automatically opens dashboard

set -e

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
CYAN='\033[0;36m'
NC='\033[0m' # No Color

# Configuration
K6_VERSION="latest"
GRAFANA_PORT=3000
INFLUXDB_PORT=8086
API_GATEWAY_URL="http://localhost:8080"

echo -e "${CYAN}╔════════════════════════════════════════╗${NC}"
echo -e "${CYAN}║  CNPM K6 Performance Test Suite       ║${NC}"
echo -e "${CYAN}║  with Grafana Dashboard               ║${NC}"
echo -e "${CYAN}╚════════════════════════════════════════╝${NC}"
echo ""

# Check if k6 is installed
if ! command -v k6 &> /dev/null; then
    echo -e "${YELLOW}⚠️  k6 is not installed. Installing...${NC}"
    if [[ "$OSTYPE" == "darwin"* ]]; then
        brew install k6
    elif [[ "$OSTYPE" == "linux-gnu"* ]]; then
        sudo apt-get update
        sudo apt-get install -y k6
    else
        echo -e "${RED}❌ Please install k6 from: https://k6.io/docs/getting-started/installation${NC}"
        exit 1
    fi
fi

echo -e "${GREEN}✅ k6 is installed${NC}"
echo ""

# Function to start Docker services
start_docker_services() {
    echo -e "${BLUE}📦 Starting Docker services (InfluxDB + Grafana)...${NC}"
    
    docker-compose -f docker-compose.k6.yml up -d
    
    sleep 5
    
    echo -e "${GREEN}✅ Docker services started${NC}"
    echo -e "${YELLOW}   Grafana: http://localhost:${GRAFANA_PORT}${NC}"
    echo -e "${YELLOW}   InfluxDB: http://localhost:${INFLUXDB_PORT}${NC}"
    echo ""
}

# Function to stop Docker services
stop_docker_services() {
    echo -e "${BLUE}🛑 Stopping Docker services...${NC}"
    docker-compose -f docker-compose.k6.yml down
    echo -e "${GREEN}✅ Docker services stopped${NC}"
    echo ""
}

# Function to run individual test
run_test() {
    local test_name=$1
    local test_file=$2
    local base_url=$3

    echo -e "${BLUE}🚀 Running: ${test_name}${NC}"
    echo -e "${YELLOW}   File: ${test_file}${NC}"
    echo -e "${YELLOW}   URL: ${base_url}${NC}"
    echo ""

    k6 run \
        --out influxdb=http://localhost:8086/k6 \
        --env BASE_URL="${base_url}" \
        --tag testname="${test_name}" \
        "${test_file}"

    echo -e "${GREEN}✅ Test completed: ${test_name}${NC}"
    echo ""
}

# Function to run all tests
run_all_tests() {
    echo -e "${CYAN}▶️  Running All Load Tests${NC}"
    echo ""

    # User Service
    run_test "User Service Load Test" \
             "./tests/user-service-load.js" \
             "http://localhost:8081"

    sleep 2

    # Product Service
    run_test "Product Service Load Test" \
             "./tests/product-service-load.js" \
             "http://localhost:8085"

    sleep 2

    # Order Service
    run_test "Order Service Load Test" \
             "./tests/order-service-load.js" \
             "http://localhost:8082"

    sleep 2

    echo -e "${GREEN}✅ All tests completed${NC}"
    echo ""
}

# Function to display dashboard instructions
show_dashboard_info() {
    echo -e "${CYAN}╔════════════════════════════════════════╗${NC}"
    echo -e "${CYAN}║  Dashboard Information                 ║${NC}"
    echo -e "${CYAN}╚════════════════════════════════════════╝${NC}"
    echo ""
    echo -e "${YELLOW}📊 Grafana Dashboard:${NC}"
    echo -e "${GREEN}   URL: http://localhost:${GRAFANA_PORT}${NC}"
    echo -e "${GREEN}   User: admin${NC}"
    echo -e "${GREEN}   Password: admin${NC}"
    echo ""
    echo -e "${YELLOW}📈 Available Dashboards:${NC}"
    echo -e "${GREEN}   • K6 Performance Metrics${NC}"
    echo -e "${GREEN}   • Request Duration Trends${NC}"
    echo -e "${GREEN}   • Error Rates${NC}"
    echo -e "${GREEN}   • Throughput Analysis${NC}"
    echo ""
    echo -e "${YELLOW}💾 InfluxDB:${NC}"
    echo -e "${GREEN}   URL: http://localhost:${INFLUXDB_PORT}${NC}"
    echo -e "${GREEN}   Database: k6${NC}"
    echo ""
}

# Main menu
show_menu() {
    echo -e "${CYAN}╔════════════════════════════════════════╗${NC}"
    echo -e "${CYAN}║  Select Test Option                   ║${NC}"
    echo -e "${CYAN}╚════════════════════════════════════════╝${NC}"
    echo ""
    echo -e "${YELLOW}1)${NC} Start Dashboard (InfluxDB + Grafana)"
    echo -e "${YELLOW}2)${NC} Run All Load Tests"
    echo -e "${YELLOW}3)${NC} Run User Service Test"
    echo -e "${YELLOW}4)${NC} Run Product Service Test"
    echo -e "${YELLOW}5)${NC} Run Order Service Test"
    echo -e "${YELLOW}6)${NC} Stop Dashboard"
    echo -e "${YELLOW}7)${NC} Show Dashboard Info"
    echo -e "${YELLOW}0)${NC} Exit"
    echo ""
    read -p "Enter choice [0-7]: " choice
}

# Main loop
while true; do
    show_menu
    
    case $choice in
        1)
            start_docker_services
            ;;
        2)
            run_all_tests
            ;;
        3)
            run_test "User Service Load Test" \
                     "./tests/user-service-load.js" \
                     "http://localhost:8081"
            ;;
        4)
            run_test "Product Service Load Test" \
                     "./tests/product-service-load.js" \
                     "http://localhost:8085"
            ;;
        5)
            run_test "Order Service Load Test" \
                     "./tests/order-service-load.js" \
                     "http://localhost:8082"
            ;;
        6)
            stop_docker_services
            ;;
        7)
            show_dashboard_info
            ;;
        0)
            echo -e "${YELLOW}👋 Goodbye!${NC}"
            exit 0
            ;;
        *)
            echo -e "${RED}❌ Invalid option${NC}"
            ;;
    esac

    echo ""
done
