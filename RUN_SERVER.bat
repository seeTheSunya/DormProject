@echo off
chcp 65001 >nul
echo Starting server...
echo.

cd /d "%~dp0sbb2"
if errorlevel 1 (
    echo Error: Cannot change to sbb2 directory.
    pause
    exit /b 1
)

if not exist "gradlew.bat" (
    echo Error: gradlew.bat file not found.
    echo Current directory: %CD%
    pause
    exit /b 1
)

echo Running in MySQL database mode...
echo.
echo Note: Make sure 'dorm' database exists in MySQL.
echo If not, create it using MySQL Workbench:
echo   CREATE DATABASE IF NOT EXISTS dorm CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
echo.

REM Check if port 8081 is in use
netstat -ano | findstr :8081 | findstr LISTENING >nul 2>&1
if not errorlevel 1 (
    echo [Warning] Port 8081 is already in use!
    echo.
    echo Process using the port:
    for /f "tokens=5" %%a in ('netstat -ano ^| findstr :8081 ^| findstr LISTENING') do (
        echo   Process ID: %%a
        tasklist /FI "PID eq %%a" /FO LIST | findstr "Image Name"
    )
    echo.
    echo Do you want to terminate the existing server and continue? (Y/N)
    choice /C YN /N /M "Select"
    if errorlevel 2 (
        echo Server start cancelled.
        pause
        exit /b 0
    )
    if errorlevel 1 (
        echo Terminating existing process...
        for /f "tokens=5" %%a in ('netstat -ano ^| findstr :8081 ^| findstr LISTENING') do (
            taskkill /PID %%a /F >nul 2>&1
        )
        timeout /t 2 /nobreak >nul
        echo.
    )
)

echo Server will be ready when you see "Started Sbb2Application" message.
echo.
echo Access http://localhost:8081/index.html in your browser.
echo.
echo Press Ctrl+C to stop the server.
echo.
echo ========================================
echo.

call gradlew.bat bootRun
if errorlevel 1 (
    echo.
    echo ========================================
    echo An error occurred. Please check the error message above.
    echo.
    
    REM Check for port conflict error
    echo | findstr /C:"Port 8081 was already in use" >nul 2>&1
    if errorlevel 1 (
        REM Not a port conflict
        echo Possible causes:
        echo 1. MySQL service may not be running.
        echo 2. MySQL password may be incorrect.
        echo 3. Port 3306 may be in use.
    ) else (
        REM Port conflict
        echo [Port Conflict Error]
        echo Port 8081 is already in use.
        echo.
        echo Solutions:
        echo 1. Stop the existing server.
        echo    - Press Ctrl+C in the previous server window.
        echo    - Or run KILL_PORT_8081.bat file.
        echo.
        echo 2. To check which process is using the port:
        echo    netstat -ano | findstr :8081
        echo.
    )
    echo.
)

pause
