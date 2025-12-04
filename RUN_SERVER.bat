@echo off
cd sbb2
echo 서버를 시작합니다...
echo.
echo H2 데이터베이스 모드로 실행 중...
echo 서버가 완전히 시작되면 "Started Sbb2Application" 메시지가 표시됩니다.
echo.
echo 브라우저에서 http://localhost:8081/index.html 접속하세요.
echo.
echo 서버를 중지하려면 Ctrl+C를 누르세요.
echo.
gradlew.bat bootRun --args=--spring.profiles.active=h2
pause

