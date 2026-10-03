@echo off
setlocal

:: -------------------------------------------------------------------
:: CONFIGURACIÓN
:: -------------------------------------------------------------------
set "DB_SERVER=LOCALHOST\SQLEXPRESS"
set "DB_NAME=NombreDeTuBase"
set "DB_USER="
set "DB_PASS="

:: Rutas de archivos y programas
set "SQL_FILE=C:\ruta\a\tu_consulta.sql"
set "OUTPUT_TXT=C:\ruta\a\resultado.txt"
set "OUTPUT_ZIP=C:\ruta\a\resultado.7z"
set "SEVENZIP_PATH=C:\Program Files\7-Zip\7z.exe"

:: -------------------------------------------------------------------
:: 1. EJECUTAR CONSULTA SQL
:: -------------------------------------------------------------------
echo Ejecutando consulta SQL...

:: Autenticación Windows (Trusted Connection)
sqlcmd -S "%DB_SERVER%" -d "%DB_NAME%" -E -i "%SQL_FILE%" -o "%OUTPUT_TXT%" -h-1 -s";" -W

:: NOTA: Si usas usuario y contraseña SQL, usa esta línea en su lugar (quita el "::"):
:: sqlcmd -S "%DB_SERVER%" -d "%DB_NAME%" -U "%DB_USER%" -P "%DB_PASS%" -i "%SQL_FILE%" -o "%OUTPUT_TXT%" -h-1 -s";" -W

if errorlevel 1 (
    echo Error al ejecutar la consulta SQL. Abortando.
    pause
    exit /b %errorlevel%
)

:: -------------------------------------------------------------------
:: 2. COMPACTAR CON 7-ZIP
:: -------------------------------------------------------------------
echo Comprimiendo archivo con 7-Zip...

"%SEVENZIP_PATH%" a -t7z "%OUTPUT_ZIP%" "%OUTPUT_TXT%"

if errorlevel 1 (
    echo Error al comprimir el archivo. No se eliminara el archivo original.
    pause
    exit /b %errorlevel%
)

:: -------------------------------------------------------------------
:: 3. ELIMINAR ARCHIVO ORIGINAL
:: -------------------------------------------------------------------
echo Eliminando archivo de texto original...
del /f /q "%OUTPUT_TXT%"

echo Proceso completado exitosamente.
endlocal