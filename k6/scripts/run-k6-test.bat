@echo off
REM K6 + Prometheus + Grafana Integration Quick Start
REM Usage: run-k6-test.bat

setlocal enabledelayedexpansion

set SCRIPT_DIR=%~dp0
set PROJECT_DIR=%SCRIPT_DIR%..
set BASE_URL=http://localhost:8081

echo.
echo ============================================
echo K6 Performance Test - CI Integration
echo ============================================
echo.

REM Check if Node.js is installed
node --version >nul 2>&1
if errorlevel 1 (
    echo ❌ Node.js not installed. Please install Node.js first.
    exit /b 1
)

REM Check if K6 is installed
k6 version >nul 2>&1
if errorlevel 1 (
    echo ❌ K6 not installed. Run: choco install k6
    exit /b 1
)

echo ✅ Node.js and K6 are installed
echo.

REM Menu
:menu
echo ============================================
echo Select an option:
echo ============================================
echo 1. Start Prometheus Exporter (port 6565)
echo 2. Run K6 Tests (all scenarios)
echo 3. Run Quick Test (1 minute)
echo 4. View Prometheus Metrics
echo 5. Open Grafana Dashboard
echo 6. View K6 Results
echo 7. Stop Prometheus Exporter
echo 8. Exit
echo ============================================

set /p choice="Enter your choice (1-8): "

if "%choice%"=="1" goto start_exporter
if "%choice%"=="2" goto run_tests
if "%choice%"=="3" goto run_quick
if "%choice%"=="4" goto view_metrics
if "%choice%"=="5" goto open_grafana
if "%choice%"=="6" goto view_results
if "%choice%"=="7" goto stop_exporter
if "%choice%"=="8" exit /b 0

echo Invalid choice. Please try again.
goto menu

:start_exporter
echo.
echo 🚀 Starting Prometheus Exporter on port 6565...
echo Press Ctrl+C to stop
echo.
cd /d "%PROJECT_DIR%"
node k6/scripts/k6-prometheus-server.js
pause
goto menu

:run_tests
echo.
echo 🎯 Running K6 Performance Tests...
echo Base URL: %BASE_URL%
echo.
cd /d "%PROJECT_DIR%"
k6 run ^
  --out json=k6-results.json ^
  --summary-export=k6-summary.json ^
  -e BASE_URL=%BASE_URL% ^
  k6/tests/k6-prometheus.js
echo.
echo ✅ Tests completed!
pause
goto menu

:run_quick
echo.
echo ⚡ Running Quick K6 Test (1 minute)...
echo.
cd /d "%PROJECT_DIR%"
k6 run ^
  --out json=k6-quick-results.json ^
  -d 1m ^
  -u 5 ^
  -e BASE_URL=%BASE_URL% ^
  k6/tests/k6-prometheus.js
echo.
echo ✅ Quick test completed!
pause
goto menu

:view_metrics
echo.
echo 📊 Fetching current metrics from Prometheus Exporter...
echo.
powershell -NoProfile -Command "& {
  try {
    $response = Invoke-WebRequest -Uri 'http://localhost:6565/metrics' -UseBasicParsing
    $response.Content | Select-String 'k6_' | ForEach-Object { $_.Line }
  } catch {
    Write-Host '❌ Cannot connect to Prometheus Exporter on port 6565'
    Write-Host '   Make sure to start it first with option 1'
  }
}"
echo.
pause
goto menu

:open_grafana
echo.
echo 🎨 Opening Grafana Dashboard...
echo URL: http://localhost:3001
echo.
start http://localhost:3001
pause
goto menu

:view_results
echo.
echo 📈 K6 Test Results Summary
echo.
if exist "%PROJECT_DIR%\k6-summary.json" (
    powershell -NoProfile -Command "& {
      Get-Content '%PROJECT_DIR%\k6-summary.json' | ConvertFrom-Json | Format-List
    }"
) else (
    echo ⚠️  No test results found. Run tests first with option 2.
)
echo.
pause
goto menu

:stop_exporter
echo.
echo 🛑 Stopping Prometheus Exporter...
taskkill /F /IM node.exe
echo ✅ Exporter stopped
pause
goto menu

echo.
pause
