@echo off
echo.
echo ============================================================
echo   ELIMINANDO CONTENEDORES Y VOLUMENES - SISTEMA ADUANAS
echo   ADVERTENCIA: Se perderan todos los datos de la BD
echo ============================================================
echo.
set /p confirm="Estas seguro? (s/n): "
if /i "%confirm%"=="s" (
    cd ..
    docker-compose down -v
    echo Contenedores y volumenes eliminados.
) else (
    echo Operacion cancelada.
)
pause
