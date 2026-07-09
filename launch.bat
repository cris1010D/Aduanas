@echo off
setlocal

:MENU
cls
echo.
echo ============================================
echo   Biblioteca - MENU PRINCIPAL
echo ============================================
echo.
echo   [1] Iniciar todos los servicios (dev)
echo   [2] Iniciar todos los servicios (test)
echo   [3] Compilar microservicios
echo   [4] Reinstalar dependencias Maven
echo.
echo   --- Servicios individuales ---
echo   [5] Iniciar Eureka
echo   [6] Iniciar ms-auth
echo   [7] Iniciar ms-proceso
echo   [8] Iniciar ms-menores
echo   [9] Iniciar ms-reporte
echo   [10] Iniciar ms-auditoria
echo   [11] Iniciar ms-sag
echo   [12] Iniciar ms-pdi
echo   [13] Iniciar ms-notaria
echo   [14] Iniciar ms-monitoreo
echo   [15] Iniciar ms-datos
echo.
echo   [0] Salir
echo.
echo ============================================
set /p opcion="  Selecciona una opcion: "

if "%opcion%"=="1" goto RUN_ALL
if "%opcion%"=="2" goto RUN_TEST
if "%opcion%"=="3" goto COMPILE
if "%opcion%"=="4" goto INSTALL
if "%opcion%"=="5" goto RUN_EUREKA
if "%opcion%"=="6" goto RUN_AUTH
if "%opcion%"=="7" goto RUN_PROCESO
if "%opcion%"=="8" goto RUN_MENORES
if "%opcion%"=="9" goto RUN_REPORTE
if "%opcion%"=="10" goto RUN_AUDITORIA
if "%opcion%"=="11" goto RUN_SAG
if "%opcion%"=="12" goto RUN_PDI
if "%opcion%"=="13" goto RUN_NOTARIA
if "%opcion%"=="14" goto RUN_MONITOREO
if "%opcion%"=="15" goto RUN_DATOS
if "%opcion%"=="0" goto SALIR

echo.
echo   Opcion invalida. Intenta de nuevo.
timeout /t 2 /nobreak > nul
goto MENU

REM ============================================

:RUN_ALL
cls
echo.
echo ===== Iniciando Eureka Server =====
start "EUREKA" mvn -f eureka spring-boot:run
timeout /t 5 /nobreak > nul
echo ===== Iniciando Microservicios =====
start "MS-AUTH" mvn -f ms-auth spring-boot:run
start "MS-PROCESO" mvn -f ms-proceso spring-boot:run
start "MS-MENORES" mvn -f ms-menores spring-boot:run
start "MS-REPORTE" mvn -f ms-reporte spring-boot:run
start "MS-AUDITORIA" mvn -f ms-auditoria spring-boot:run
start "MS-SAG" mvn -f ms-sag spring-boot:run
start "MS-PDI" mvn -f ms-pdi spring-boot:run
start "MS-NOTARIA" mvn -f ms-notaria spring-boot:run
start "MS-MONITOREO" mvn -f ms-monitoreo spring-boot:run
start "MS-DATOS" mvn -f ms-datos spring-boot:run
echo Todos los servicios han sido lanzados.
pause
goto MENU

:RUN_TEST
cls
echo.
echo ===== Iniciando Eureka Server (test) =====
start "EUREKA" java -jar eureka\target\cl-triskeledu-eureka-1.0-SNAPSHOT.jar --spring.profiles.active=test
timeout /t 5 /nobreak > nul
echo ===== Iniciando Microservicios (test) =====
start "MS-AUTH" java -jar ms-auth\\target\\cl-triskeledu-auth-0.0.1-SNAPSHOT.jar --spring.profiles.active=test
start "MS-PROCESO" java -jar ms-proceso\\target\\cl-triskeledu-proceso-0.0.1-SNAPSHOT.jar --spring.profiles.active=test
start "MS-MENORES" java -jar ms-menores\\target\\cl-triskeledu-menores-0.0.1-SNAPSHOT.jar --spring.profiles.active=test
start "MS-REPORTE" java -jar ms-reporte\\target\\cl-triskeledu-reporte-0.0.1-SNAPSHOT.jar --spring.profiles.active=test
start "MS-AUDITORIA" java -jar ms-auditoria\\target\\cl-triskeledu-auditoria-0.0.1-SNAPSHOT.jar --spring.profiles.active=test
start "MS-SAG" java -jar ms-sag\\target\\cl-triskeledu-sag-0.0.1-SNAPSHOT.jar --spring.profiles.active=test
start "MS-PDI" java -jar ms-pdi\\target\\cl-triskeledu-pdi-0.0.1-SNAPSHOT.jar --spring.profiles.active=test
start "MS-NOTARIA" java -jar ms-notaria\\target\\cl-triskeledu-notaria-0.0.1-SNAPSHOT.jar --spring.profiles.active=test
start "MS-MONITOREO" java -jar ms-monitoreo\\target\\cl-triskeledu-monitoreo-0.0.1-SNAPSHOT.jar --spring.profiles.active=test
start "MS-DATOS" java -jar ms-datos\\target\\cl-triskeledu-datos-0.0.1-SNAPSHOT.jar --spring.profiles.active=test
echo Todos los servicios han sido lanzados en modo test.
pause
goto MENU

:COMPILE
cls
echo.
echo ===== Compilando microservicios =====
cd /d C:\Aduanas\ms-auth
call mvn clean install -U
cd /d C:\Aduanas\ms-proceso
call mvn clean install -U
cd /d C:\Aduanas\ms-menores
call mvn clean install -U
cd /d C:\Aduanas\ms-reporte
call mvn clean install -U
cd /d C:\Aduanas\ms-auditoria
call mvn clean install -U
cd /d C:\Aduanas\ms-sag
call mvn clean install -U
cd /d C:\Aduanas\ms-pdi
call mvn clean install -U
cd /d C:\Aduanas\ms-notaria
call mvn clean install -U
cd /d C:\Aduanas\ms-monitoreo
call mvn clean install -U
cd /d C:\Aduanas\ms-datos
call mvn clean install -U
echo Compilacion completada.
pause
goto MENU

:INSTALL
cls
echo.
echo === REINSTALACION DE DEPENDENCIAS MAVEN ===
echo.
echo Eliminando carpeta .m2 ...
rmdir /s /q %USERPROFILE%\.m2
echo Eliminando carpetas target ...
rmdir /s /q C:\Aduanas\eureka\target
rmdir /s /q C:\Aduanas\ms-auth\target
rmdir /s /q C:\Aduanas\ms-proceso\target
rmdir /s /q C:\Aduanas\ms-menores\target
rmdir /s /q C:\Aduanas\ms-reporte\target
rmdir /s /q C:\Aduanas\ms-auditoria\target
rmdir /s /q C:\Aduanas\ms-sag\target
rmdir /s /q C:\Aduanas\ms-pdi\target
rmdir /s /q C:\Aduanas\ms-notaria\target
rmdir /s /q C:\Aduanas\ms-monitoreo\target
rmdir /s /q C:\Aduanas\ms-datos\target
echo Descargando dependencias nuevamente con Maven ...
mvn clean install -U -DskipTests
echo.
echo === PROCESO COMPLETADO ===
pause
goto MENU

:RUN_EUREKA
cls
echo.
echo ===== Iniciando Eureka =====
start "EUREKA" mvn -f eureka spring-boot:run
echo Eureka iniciado.
pause
goto MENU

:RUN_AUTH
cls
echo.
echo ===== Iniciando ms-auth =====
start "MS-AUTH" mvn -f ms-auth spring-boot:run
echo ms-auth iniciado.
pause
goto MENU

:RUN_PROCESO
cls
echo.
echo ===== Iniciando ms-proceso =====
start "MS-PROCESO" mvn -f ms-proceso spring-boot:run
echo ms-proceso iniciado.
pause
goto MENU

:RUN_MENORES
cls
echo.
echo ===== Iniciando ms-menores =====
start "MS-MENORES" mvn -f ms-menores spring-boot:run
echo ms-menores iniciado.
pause
goto MENU

:RUN_REPORTE
cls
echo.
echo ===== Iniciando ms-reporte =====
start "MS-REPORTE" mvn -f ms-reporte spring-boot:run
echo ms-reporte iniciado.
pause
goto MENU

:RUN_AUDITORIA
cls
echo.
echo ===== Iniciando ms-auditoria =====
start "MS-AUDITORIA" mvn -f ms-auditoria spring-boot:run
echo ms-auditoria iniciado.
pause
goto MENU

:RUN_SAG
cls
echo.
echo ===== Iniciando ms-sag =====
start "MS-SAG" mvn -f ms-sag spring-boot:run
echo ms-sag iniciado.
pause
goto MENU

:RUN_PDI
cls
echo.
echo ===== Iniciando ms-pdi =====
start "MS-PDI" mvn -f ms-pdi spring-boot:run
echo ms-pdi iniciado.
pause
goto MENU

:RUN_NOTARIA
cls
echo.
echo ===== Iniciando ms-notaria =====
start "MS-NOTARIA" mvn -f ms-notaria spring-boot:run
echo ms-notaria iniciado.
pause
goto MENU

:RUN_MONITOREO
cls
echo.
echo ===== Iniciando ms-monitoreo =====
start "MS-MONITOREO" mvn -f ms-monitoreo spring-boot:run
echo ms-monitoreo iniciado.
pause
goto MENU

:RUN_DATOS
cls
echo.
echo ===== Iniciando ms-datos =====
start "MS-DATOS" mvn -f ms-datos spring-boot:run
echo ms-datos iniciado.
pause
goto MENU

:SALIR
cls
echo.
echo   Hasta luego.
echo.
endlocal
exit /b
