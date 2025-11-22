@echo off
REM CNPM K6 Performance Test Runner (Windows)
REM This script runs k6 performance tests with Grafana dashboard

setlocal enabledelayedexpansion

REM Colors
set RED=[91m
set GREEN=[92m
set YELLOW=[93m
set BLUE=[94m
set CYAN=[96m
set RESET=[0m

echo.
echo %CYAN%╔════════════════════════════════════════╗%RESET%
echo %CYAN%║  CNPM K6 Performance Test Suite       ║%RESET%
echo %CYAN%║  with Grafana Dashboard (Windows)     ║%RESET%
echo %CYAN%╚════════════════════════════════════════╝%RESET%
echo.

REM Check if k6 is installed
where k6 >nul 2>nul
if %ERRORLEVEL% neq 0 (
    echo %YELLOW%⚠️  k6 is not installed. Please install from: https://k6.io/docs/getting-started/installation%RESET%
    pause
    exit /b 1
)

echo %GREEN%✅ k6 is installed%RESET%
echo.

REM Main menu
:menu
echo %CYAN%╔════════════════════════════════════════╗%RESET%
echo %CYAN%║  Select Test Option                   ║%RESET%
echo %CYAN%╚════════════════════════════════════════╝%RESET%
echo.
echo %YELLOW%1) Start Dashboard ^(InfluxDB + Grafana^)%RESET%
echo %YELLOW%2) Run All Load Tests%RESET%
echo %YELLOW%3) Run User Service Test%RESET%
echo %YELLOW%4) Run Product Service Test%RESET%
echo %YELLOW%5) Run Order Service Test%RESET%
echo %YELLOW%6) Stop Dashboard%RESET%
echo %YELLOW%7) Show Dashboard Info%RESET%
echo %YELLOW%0) Exit%RESET%
echo.
set /p choice="Enter choice [0-7]: "

if "%choice%"=="1" goto start_dashboard
if "%choice%"=="2" goto run_all
if "%choice%"=="3" goto run_user
if "%choice%"=="4" goto run_product
if "%choice%"=="5" goto run_order
if "%choice%"=="6" goto stop_dashboard
if "%choice%"=="7" goto show_info
if "%choice%"=="0" goto exit_app
echo %RED%❌ Invalid option%RESET%
goto menu

:start_dashboard
echo.
echo %BLUE%📦 Starting Docker services ^(InfluxDB + Grafana^)...%RESET%
docker-compose -f docker-compose.k6.yml up -d
timeout /t 5 /nobreak
echo %GREEN%✅ Docker services started%RESET%
echo %YELLOW%   Grafana: http://localhost:3000%RESET%
echo %YELLOW%   InfluxDB: http://localhost:8086%RESET%
echo.
start http://localhost:3000
goto menu

:run_all
echo.
echo %BLUE%🚀 Running All Load Tests...%RESET%
echo.
call :run_user_test
timeout /t 2 /nobreak
call :run_product_test
timeout /t 2 /nobreak
call :run_order_test
echo %GREEN%✅ All tests completed%RESET%
echo.
goto menu

:run_user
echo.
call :run_user_test
echo.
goto menu

:run_product
echo.
call :run_product_test
echo.
goto menu

:run_order
echo.
call :run_order_test
echo.
goto menu

:run_user_test
echo %BLUE%🚀 Running: User Service Load Test%RESET%
echo %YELLOW%   File: tests/user-service-load.js%RESET%
echo %YELLOW%   URL: http://localhost:8081%RESET%
echo.
k6 run --out influxdb=http://localhost:8086/k6 --env BASE_URL="http://localhost:8081" --tag testname="User Service Load Test" .\tests\user-service-load.js
echo %GREEN%✅ Test completed: User Service Load Test%RESET%
exit /b

:run_product_test
echo %BLUE%🚀 Running: Product Service Load Test%RESET%
echo %YELLOW%   File: tests/product-service-load.js%RESET%
echo %YELLOW%   URL: http://localhost:8085%RESET%
echo.
k6 run --out influxdb=http://localhost:8086/k6 --env BASE_URL="http://localhost:8085" --tag testname="Product Service Load Test" .\tests\product-service-load.js
echo %GREEN%✅ Test completed: Product Service Load Test%RESET%
exit /b

:run_order_test
echo %BLUE%🚀 Running: Order Service Load Test%RESET%
echo %YELLOW%   File: tests/order-service-load.js%RESET%
echo %YELLOW%   URL: http://localhost:8082%RESET%
echo.
k6 run --out influxdb=http://localhost:8086/k6 --env BASE_URL="http://localhost:8082" --tag testname="Order Service Load Test" .\tests\order-service-load.js
echo %GREEN%✅ Test completed: Order Service Load Test%RESET%
exit /b

:stop_dashboard
echo.
echo %BLUE%🛑 Stopping Docker services...%RESET%
docker-compose -f docker-compose.k6.yml down
echo %GREEN%✅ Docker services stopped%RESET%
echo.
goto menu

:show_info
echo.
echo %CYAN%╔════════════════════════════════════════╗%RESET%
echo %CYAN%║  Dashboard Information                 ║%RESET%
echo %CYAN%╚════════════════════════════════════════╝%RESET%
echo.
echo %YELLOW%📊 Grafana Dashboard:%RESET%
echo %GREEN%   URL: http://localhost:3000%RESET%
echo %GREEN%   User: admin%RESET%
echo %GREEN%   Password: admin%RESET%
echo.
echo %YELLOW%📈 Available Dashboards:%RESET%
echo %GREEN%   • K6 Performance Metrics%RESET%
echo %GREEN%   • Request Duration Trends%RESET%
echo %GREEN%   • Error Rates%RESET%
echo %GREEN%   • Throughput Analysis%RESET%
echo.
echo %YELLOW%💾 InfluxDB:%RESET%
echo %GREEN%   URL: http://localhost:8086%RESET%
echo %GREEN%   Database: k6%RESET%
echo.
pause
goto menu

:exit_app
echo.
echo %YELLOW%👋 Goodbye!%RESET%
exit /b 0
