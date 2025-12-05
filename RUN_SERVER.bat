@echo off
chcp 65001 >nul
echo 서버를 시작합니다...
echo.

cd /d "%~dp0sbb2"
if errorlevel 1 (
    echo 오류: sbb2 디렉토리로 이동할 수 없습니다.
    pause
    exit /b 1
)

if not exist "gradlew.bat" (
    echo 오류: gradlew.bat 파일을 찾을 수 없습니다.
    echo 현재 디렉토리: %CD%
    pause
    exit /b 1
)

echo MySQL 데이터베이스 모드로 실행 중...
echo.

REM 포트 8081 사용 여부 확인
netstat -ano | findstr :8081 | findstr LISTENING >nul 2>&1
if not errorlevel 1 (
    echo [경고] 포트 8081이 이미 사용 중입니다!
    echo.
    echo 포트를 사용하는 프로세스:
    for /f "tokens=5" %%a in ('netstat -ano ^| findstr :8081 ^| findstr LISTENING') do (
        echo   프로세스 ID: %%a
        tasklist /FI "PID eq %%a" /FO LIST | findstr "Image Name"
    )
    echo.
    echo 기존 서버를 종료하고 계속하시겠습니까? (Y/N)
    choice /C YN /N /M "선택"
    if errorlevel 2 (
        echo 서버 시작을 취소했습니다.
        pause
        exit /b 0
    )
    if errorlevel 1 (
        echo 기존 프로세스를 종료하는 중...
        for /f "tokens=5" %%a in ('netstat -ano ^| findstr :8081 ^| findstr LISTENING') do (
            taskkill /PID %%a /F >nul 2>&1
        )
        timeout /t 2 /nobreak >nul
        echo.
    )
)

echo 서버가 완전히 시작되면 "Started Sbb2Application" 메시지가 표시됩니다.
echo.
echo 브라우저에서 http://localhost:8081/index.html 접속하세요.
echo.
echo 서버를 중지하려면 Ctrl+C를 누르세요.
echo.
echo ========================================
echo.

call gradlew.bat bootRun
if errorlevel 1 (
    echo.
    echo ========================================
    echo 오류가 발생했습니다. 위의 오류 메시지를 확인하세요.
    echo.
    
    REM 포트 충돌 오류 확인
    echo | findstr /C:"Port 8081 was already in use" >nul 2>&1
    if errorlevel 1 (
        REM 포트 충돌이 아닌 경우
        echo 가능한 원인:
        echo 1. MySQL 서비스가 실행 중이 아닐 수 있습니다.
        echo 2. MySQL 비밀번호가 잘못되었을 수 있습니다.
        echo 3. 포트 3306이 사용 중일 수 있습니다.
    ) else (
        REM 포트 충돌인 경우
        echo [포트 충돌 오류]
        echo 포트 8081이 이미 사용 중입니다.
        echo.
        echo 해결 방법:
        echo 1. 기존에 실행 중인 서버를 종료하세요.
        echo    - 이전에 실행한 서버 창에서 Ctrl+C를 누르세요.
        echo    - 또는 KILL_PORT_8081.bat 파일을 실행하세요.
        echo.
        echo 2. 포트를 사용하는 프로세스를 확인하려면:
        echo    netstat -ano | findstr :8081
        echo.
    )
    echo.
)

pause




