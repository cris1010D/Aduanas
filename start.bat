@echo off
echo.
echo =====================================================
echo   ADUANAS - INICIANDO CONTENEDORES
echo =====================================================
echo.
cd /d C:\Aduanas
docker compose up -d
echo.
echo Contenedores iniciando...
echo Espera 3-4 minutos a que todos esten HEALTHY.
echo.
echo Verifica el estado con:  docker ps
echo.
echo Gateway:    http://localhost:9000
echo Swagger:    http://localhost:9000/swagger-ui.html
echo Dashboard:  C:\Aduanas\dashboard.html
echo.
pause
