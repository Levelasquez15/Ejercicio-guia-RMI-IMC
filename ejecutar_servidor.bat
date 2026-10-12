@echo off
chcp 65001 > nul
echo ============================================================
echo   INICIANDO SERVIDOR RMI - IMC (LipeRMI)
echo ============================================================

if not exist "EjemploLipeRmiServidorImc\dist\EjemploLipeRmiServidorImc.jar" (
    echo [*] Compilando componentes requeridos...
    call compilar.bat
)

java -cp "EjemploLipeRmiLibImc\dist\EjemploLipeRmiLibImc.jar;lib\lipermi-1.0.1.jar;EjemploLipeRmiServidorImc\dist\EjemploLipeRmiServidorImc.jar" johnarrieta.seminario.imc.rmi.Principal
pause
