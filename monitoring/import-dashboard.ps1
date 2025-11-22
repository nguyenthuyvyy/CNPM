# Import Grafana Dashboard Script (PowerShell)
# This script imports the CNPM dashboard into Grafana

$GRAFANA_URL = "http://localhost:3001"
$GRAFANA_ADMIN_USER = "admin"
$GRAFANA_ADMIN_PASSWORD = "admin"
$DASHBOARD_FILE = "$PSScriptRoot\grafana-dashboard.json"
$DASHBOARD_JSON = Get-Content $DASHBOARD_FILE -Raw

Write-Host "🔄 Importing CNPM Dashboard to Grafana..." -ForegroundColor Cyan

# Encode credentials
$Pair = "$($GRAFANA_ADMIN_USER):$($GRAFANA_ADMIN_PASSWORD)"
$EncodedPair = [System.Convert]::ToBase64String([System.Text.Encoding]::ASCII.GetBytes($Pair))
$Headers = @{
    "Authorization" = "Basic $EncodedPair"
    "Content-Type" = "application/json"
}

# Setup Prometheus datasource
Write-Host "✅ Setting up Prometheus datasource..." -ForegroundColor Green

$DatasourceBody = @{
    name = "Prometheus"
    type = "prometheus"
    access = "proxy"
    url = "http://prometheus:9090"
    isDefault = $true
    jsonData = @{}
} | ConvertTo-Json

try {
    $response = Invoke-WebRequest -Uri "$GRAFANA_URL/api/datasources" `
        -Method POST `
        -Headers $Headers `
        -Body $DatasourceBody `
        -UseBasicParsing -ErrorAction SilentlyContinue
    Write-Host "✅ Datasource created" -ForegroundColor Green
} catch {
    Write-Host "⚠️  Datasource may already exist" -ForegroundColor Yellow
}

# Import dashboard
Write-Host "📊 Importing dashboard..." -ForegroundColor Cyan

$DashboardBody = @{
    dashboard = ($DASHBOARD_JSON | ConvertFrom-Json)
    overwrite = $true
} | ConvertTo-Json -Depth 100

try {
    $response = Invoke-WebRequest -Uri "$GRAFANA_URL/api/dashboards/db" `
        -Method POST `
        -Headers $Headers `
        -Body $DashboardBody `
        -UseBasicParsing
    
    $result = $response.Content | ConvertFrom-Json
    Write-Host "✅ Dashboard imported successfully!" -ForegroundColor Green
    Write-Host "🌐 Access Grafana at: $GRAFANA_URL" -ForegroundColor Cyan
    Write-Host "📊 Dashboard UID: $($result.uid)" -ForegroundColor Yellow
} catch {
    Write-Host "❌ Error importing dashboard: $_" -ForegroundColor Red
    Write-Host "Response: $($_.Exception.Response.StatusCode)" -ForegroundColor Red
}
