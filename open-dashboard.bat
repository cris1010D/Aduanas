@echo off
echo.
echo Iniciando servidor local para el dashboard...
echo Abre http://localhost:8888/dashboard.html en Chrome
echo (Ctrl+C para cerrar el servidor cuando termines)
echo.
cd /d C:\Aduanas
python -m http.server 8888
