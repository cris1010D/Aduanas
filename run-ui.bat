@echo off
cd /d "%~dp0ui-web"
echo Iniciando servidor UI en http://localhost:8181
echo Presiona Ctrl+C para detener.
python -m http.server 8181
