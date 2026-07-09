@echo off
setlocal enabledelayedexpansion
echo.
echo =====================================================
echo   ADUANAS - BUILD Y DESPLIEGUE DOCKER
echo =====================================================
echo.
echo IMPORTANTE: Asegurate de tener VS Code CERRADO antes
echo de continuar. El Language Server de VS Code puede
echo corromper los JARs e impedir la generacion de MapStruct.
echo.
pause

cd /d C:\Aduanas

REM Paso 1: Compilar common primero (dependencia de todos)
echo.
echo [1/4] Compilando modulo common...
cd /d C:\Aduanas\common
call mvn clean install -DskipTests
if %errorlevel% neq 0 (
    echo.
    echo ERROR: Fallo la compilacion de common.
    pause & exit /b 1
)
echo [OK] common compilado.

REM Paso 2: Compilar todos los microservicios
echo.
echo [2/4] Compilando microservicios con Maven...
echo       (Esto puede tardar 5-10 minutos la primera vez)
echo.

for %%S in (eureka api-gateway ms-auth ms-proceso ms-menores ms-reporte ms-auditoria ms-sag ms-pdi ms-notaria ms-monitoreo ms-datos) do (
    echo -----------------------------------------------
    echo  Compilando %%S...
    echo -----------------------------------------------
    cd /d C:\Aduanas\%%S
    call mvn clean package -DskipTests
    if !errorlevel! neq 0 (
        echo ERROR: Fallo la compilacion de %%S.
        pause & exit /b 1
    )
    echo [OK] %%S compilado.
    echo.
)

REM Paso 3: Construir imagenes Docker individualmente
echo.
echo [3/4] Construyendo imagenes Docker...
echo       (Esto puede tardar varios minutos)
echo.

for %%S in (eureka api-gateway ms-auth ms-proceso ms-menores ms-reporte ms-auditoria ms-sag ms-pdi ms-notaria ms-monitoreo ms-datos) do (
    echo Construyendo imagen aduanas/%%S:latest...
    docker build -t aduanas/%%S:latest C:\Aduanas\%%S
    if !errorlevel! neq 0 (
        echo ERROR: Fallo el build Docker de %%S.
        pause & exit /b 1
    )
    echo [OK] aduanas/%%S listo.
    echo.
)

REM Paso 4: Levantar contenedores
echo.
echo [4/4] Levantando contenedores...
cd /d C:\Aduanas
docker compose down --remove-orphans
docker compose up -d

if %errorlevel% neq 0 (
    echo ERROR: Fallo docker compose up.
    pause & exit /b 1
)

echo.
echo =====================================================
echo   CONTENEDORES INICIADOS CORRECTAMENTE
echo =====================================================
echo.
echo Gateway:    http://localhost:9000
echo Swagger:    http://localhost:9000/swagger-ui.html
echo Dashboard:  C:\Aduanas\dashboard.html
echo.
echo Espera 3-4 minutos a que todos los MS esten HEALTHY.
echo Verifica con: docker ps
echo.
pause
