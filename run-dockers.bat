@echo off
docker compose down
docker volume prune -f
docker compose up -d
timeout /t 10
cd init-multi-db
call docker_compile_dbs.bat
cd ..
echo Infraestructura lista.
pause