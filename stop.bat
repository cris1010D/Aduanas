@echo off
echo.
echo =====================================================
echo   ADUANAS - APAGANDO CONTENEDORES
echo =====================================================
echo.
cd /d C:\Aduanas
docker compose down
echo.
echo Todos los contenedores apagados correctamente.
echo Para volver a iniciar usa start.bat
echo.
pause
