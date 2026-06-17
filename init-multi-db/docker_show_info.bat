@echo off
echo.
echo ============================================================
echo   ESTADO DE CONTENEDORES - SISTEMA ADUANAS
echo ============================================================
echo.
docker ps --format "table {{.Names}}\t{{.Status}}\t{{.Ports}}" --filter "network=aduanas-network"
echo.
echo ============================================================
echo   VOLUMENES
echo ============================================================
docker volume ls --filter "name=aduanas"
echo.
pause
