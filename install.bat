@echo off
echo.
echo === REINSTALACION DE DEPENDENCIAS MAVEN ===
echo.

REM Paso 1: Eliminar carpeta local de dependencias
echo Eliminando carpeta .m2 ...
rmdir /s /q %USERPROFILE%\.m2

REM Paso 2: Eliminar carpetas target de los proyectos
echo Eliminando carpetas target ...
rmdir /s /q C:\Aduanas\eureka\target
rmdir /s /q C:\Aduanas\common\target
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

REM Paso 3: Instalar todas las dependencias forzadamente
echo Descargando dependencias nuevamente con Maven ...
cd /d C:\Aduanas
mvn clean install -U -DskipTests

echo.
echo === PROCESO COMPLETADO ===
pause
