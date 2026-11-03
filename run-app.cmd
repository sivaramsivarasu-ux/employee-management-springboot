@echo off
setlocal

cd /d "%~dp0"
where java >nul 2>nul
if errorlevel 1 (
    echo Java 25 or later was not found on PATH. Install a JDK and configure JAVA_HOME or PATH.
    exit /b 1
)

where mvn >nul 2>nul
if errorlevel 1 (
    echo Maven was not found. Install Maven and add it to PATH.
    exit /b 1
)

call mvn spring-boot:run
exit /b %ERRORLEVEL%
