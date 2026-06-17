@echo off
echo.
echo ============================================================
echo   LEVANTANDO INFRAESTRUCTURA - SISTEMA ADUANAS
echo   PostgreSQL:5433  Zookeeper:2181  Kafka:9092
echo ============================================================
echo.
cd ..
docker-compose up -d
echo.
echo Infraestructura iniciada. Verificar estado con docker ps
pause
