@echo off
echo.
echo ============================================================
echo   EJECUTANDO SCRIPTS SQL EN ADUANAS-POSTGRES
echo   Requiere que el contenedor este corriendo:
echo   docker-compose up -d (desde C:\Aduanas)
echo ============================================================
echo.

echo [1/11] Creando bases de datos...
docker exec -i aduanas-postgres psql -U postgres -d postgres < 01-init.sql

echo [2/11] Creando esquema AUTH...
docker exec -i aduanas-postgres psql -U postgres -d postgres < 02-create_auth.sql

echo [3/11] Creando esquema PROCESO...
docker exec -i aduanas-postgres psql -U postgres -d postgres < 03-create_proceso.sql

echo [4/11] Creando esquema MENORES...
docker exec -i aduanas-postgres psql -U postgres -d postgres < 04-create_menores.sql

echo [5/11] Creando esquema REPORTE...
docker exec -i aduanas-postgres psql -U postgres -d postgres < 05-create_reporte.sql

echo [6/11] Creando esquema AUDITORIA...
docker exec -i aduanas-postgres psql -U postgres -d postgres < 06-create_auditoria.sql

echo [7/11] Creando esquema SAG...
docker exec -i aduanas-postgres psql -U postgres -d postgres < 07-create_sag.sql

echo [8/11] Creando esquema PDI...
docker exec -i aduanas-postgres psql -U postgres -d postgres < 08-create_pdi.sql

echo [9/11] Creando esquema NOTARIA...
docker exec -i aduanas-postgres psql -U postgres -d postgres < 09-create_notaria.sql

echo [10/11] Creando esquema MONITOREO...
docker exec -i aduanas-postgres psql -U postgres -d postgres < 10-create_monitoreo.sql

echo [11/11] Creando esquema DATOS...
docker exec -i aduanas-postgres psql -U postgres -d postgres < 11-create_datos.sql

echo.
echo ============================================================
echo   SCRIPTS EJECUTADOS CORRECTAMENTE
echo ============================================================
pause
