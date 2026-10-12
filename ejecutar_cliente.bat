@echo off
chcp 65001 > nul
echo ============================================================
echo   INICIANDO CLIENTE RMI - IMC (Swing GUI)
echo ============================================================

if not exist "EjemploLipeRmiClienteImc\dist\EjemploLipeRmiClienteImc.jar" (
    echo [*] Compilando componentes requeridos...
    call compilar.bat
)

start javaw -cp "EjemploLipeRmiLibImc\dist\EjemploLipeRmiLibImc.jar;lib\lipermi-1.0.1.jar;EjemploLipeRmiClienteImc\dist\EjemploLipeRmiClienteImc.jar" johnarrieta.seminario.imc.rmi.Principal
