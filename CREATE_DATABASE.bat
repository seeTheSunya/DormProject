@echo off
chcp 65001 >nul
echo Creating database if not exists...
echo.

REM MySQL 설치 경로 확인 (일반적인 경로들)
set MYSQL_PATH=
if exist "C:\Program Files\MySQL\MySQL Server 9.5\bin\mysql.exe" (
    set MYSQL_PATH=C:\Program Files\MySQL\MySQL Server 9.5\bin\mysql.exe
) else if exist "C:\Program Files (x86)\MySQL\MySQL Server 9.5\bin\mysql.exe" (
    set MYSQL_PATH=C:\Program Files (x86)\MySQL\MySQL Server 9.5\bin\mysql.exe
) else if exist "C:\Program Files\MySQL\MySQL Server 9.0\bin\mysql.exe" (
    set MYSQL_PATH=C:\Program Files\MySQL\MySQL Server 9.0\bin\mysql.exe
) else if exist "C:\Program Files\MySQL\MySQL Server 8.0\bin\mysql.exe" (
    set MYSQL_PATH=C:\Program Files\MySQL\MySQL Server 8.0\bin\mysql.exe
)

if defined MYSQL_PATH (
    "%MYSQL_PATH%" -u root -pjjs47532@ -e "CREATE DATABASE IF NOT EXISTS dorm CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;" 2>nul
    if errorlevel 1 (
        echo Warning: Could not create database automatically.
        echo Please create 'dorm' database manually using MySQL Workbench.
        echo.
    ) else (
        echo Database 'dorm' created/verified successfully!
        echo.
    )
) else (
    echo MySQL command not found in standard locations.
    echo Skipping database creation - Spring Boot will attempt to create it.
    echo.
)

exit /b 0
