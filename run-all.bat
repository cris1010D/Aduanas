@echo off
echo.
echo ============================================================
echo   SISTEMA DE CONTROL FRONTERIZO - ADUANAS
echo   Iniciando todos los microservicios...
echo   Requiere: Docker corriendo (docker-compose up -d)
echo ============================================================
echo.

echo [1/11] Iniciando Eureka Server...
start "eureka"      mvn -f eureka      spring-boot:run
timeout /t 20 /nobreak > nul

echo [2/11] Iniciando ms-auth...
start "ms-auth"     mvn -f ms-auth     spring-boot:run
timeout /t 8 /nobreak > nul

echo [3/11] Iniciando ms-proceso...
start "ms-proceso"  mvn -f ms-proceso  spring-boot:run
timeout /t 8 /nobreak > nul

echo [4/11] Iniciando ms-menores...
start "ms-menores"  mvn -f ms-menores  spring-boot:run
timeout /t 8 /nobreak > nul

echo [5/11] Iniciando ms-reporte...
start "ms-reporte"  mvn -f ms-reporte  spring-boot:run
timeout /t 8 /nobreak > nul

echo [6/11] Iniciando ms-auditoria...
start "ms-auditoria" mvn -f ms-auditoria spring-boot:run
timeout /t 8 /nobreak > nul

echo [7/11] Iniciando ms-sag...
start "ms-sag"      mvn -f ms-sag      spring-boot:run
timeout /t 8 /nobreak > nul

echo [8/11] Iniciando ms-pdi...
start "ms-pdi"      mvn -f ms-pdi      spring-boot:run
timeout /t 8 /nobreak > nul

echo [9/11] Iniciando ms-notaria...
start "ms-notaria"  mvn -f ms-notaria  spring-boot:run
timeout /t 8 /nobreak > nul

echo [10/11] Iniciando ms-monitoreo...
start "ms-monitoreo" mvn -f ms-monitoreo spring-boot:run
timeout /t 8 /nobreak > nul

echo [11/11] Iniciando ms-datos...
start "ms-datos"    mvn -f ms-datos    spring-boot:run

echo.
echo ============================================================
echo   Todos los servicios han sido lanzados en ventanas
echo   separadas. Espera ~60s para que todos esten listos.
echo.
echo   Eureka Dashboard: http://localhost:8761
echo   Kafka UI:         http://localhost:8080
echo ============================================================
echo.
pause
