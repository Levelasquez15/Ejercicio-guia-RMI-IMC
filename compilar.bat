@echo off
chcp 65001 > nul
echo ============================================================
echo   COMPILANDO SISTEMA DISTRIBUIDO RMI - IMC (LipeRMI)
echo ============================================================

set "NETBEANS_ANT=C:\Program Files\NetBeans-25\netbeans\extide\ant\bin\ant.bat"

if exist "%NETBEANS_ANT%" (
    echo [*] Compilando con Apache Ant de NetBeans...
    call "%NETBEANS_ANT%" -f EjemploLipeRmiLibImc clean jar
    if errorlevel 1 goto error
    call "%NETBEANS_ANT%" -f EjemploLipeRmiServidorImc clean jar
    if errorlevel 1 goto error
    call "%NETBEANS_ANT%" -f EjemploLipeRmiClienteImc clean jar
    if errorlevel 1 goto error
) else (
    echo [*] Compilando directamente con javac...
    if not exist "EjemploLipeRmiLibImc\dist" mkdir "EjemploLipeRmiLibImc\dist"
    if not exist "EjemploLipeRmiLibImc\build\classes" mkdir "EjemploLipeRmiLibImc\build\classes"
    javac -encoding UTF-8 -d "EjemploLipeRmiLibImc\build\classes" EjemploLipeRmiLibImc\src\johnarrieta\seminario\imc\rmi\lib\*.java
    jar cf EjemploLipeRmiLibImc\dist\EjemploLipeRmiLibImc.jar -C EjemploLipeRmiLibImc\build\classes .

    if not exist "EjemploLipeRmiServidorImc\dist" mkdir "EjemploLipeRmiServidorImc\dist"
    if not exist "EjemploLipeRmiServidorImc\build\classes" mkdir "EjemploLipeRmiServidorImc\build\classes"
    javac -encoding UTF-8 -cp "EjemploLipeRmiLibImc\dist\EjemploLipeRmiLibImc.jar;lib\lipermi-1.0.1.jar" -d "EjemploLipeRmiServidorImc\build\classes" EjemploLipeRmiServidorImc\src\johnarrieta\seminario\imc\rmi\net\*.java EjemploLipeRmiServidorImc\src\johnarrieta\seminario\imc\rmi\*.java
    jar cfe EjemploLipeRmiServidorImc\dist\EjemploLipeRmiServidorImc.jar johnarrieta.seminario.imc.rmi.Principal -C EjemploLipeRmiServidorImc\build\classes .

    if not exist "EjemploLipeRmiClienteImc\dist" mkdir "EjemploLipeRmiClienteImc\dist"
    if not exist "EjemploLipeRmiClienteImc\build\classes" mkdir "EjemploLipeRmiClienteImc\build\classes"
    javac -encoding UTF-8 -cp "EjemploLipeRmiLibImc\dist\EjemploLipeRmiLibImc.jar;lib\lipermi-1.0.1.jar" -d "EjemploLipeRmiClienteImc\build\classes" EjemploLipeRmiClienteImc\src\johnarrieta\seminario\imc\rmi\vistas\*.java EjemploLipeRmiClienteImc\src\johnarrieta\seminario\imc\rmi\*.java
    jar cfe EjemploLipeRmiClienteImc\dist\EjemploLipeRmiClienteImc.jar johnarrieta.seminario.imc.rmi.Principal -C EjemploLipeRmiClienteImc\build\classes .
)

echo.
echo ============================================================
echo   COMPILACION FINALIZADA CON EXITO
echo ============================================================
pause
exit /b 0

:error
echo [ERROR] Hubo un fallo en la compilacion.
pause
exit /b 1
