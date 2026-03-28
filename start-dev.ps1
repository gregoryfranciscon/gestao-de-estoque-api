$projectRoot = Split-Path -Parent $MyInvocation.MyCommand.Path

$backendCommand = "Set-Location -LiteralPath '$projectRoot'; .\mvnw.cmd spring-boot:run"

Start-Process powershell -ArgumentList "-NoExit", "-Command", $backendCommand

Write-Host "Aplicacao iniciando em uma janela separada."
Write-Host "Abra http://localhost:8080 quando o Spring Boot terminar de subir."
