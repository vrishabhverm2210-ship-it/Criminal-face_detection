@echo off
REM ============================================================
REM  CriminalFaceDB — Windows Build & Run Script
REM  Requirements: JDK 11+, MySQL 8+, mysql-connector-j.jar in /lib
REM ============================================================

SET SRC=src
SET BIN=bin
SET LIB=lib\mysql-connector-j.jar

REM Create output dir
if not exist %BIN% mkdir %BIN%

echo [1/3] Compiling Java sources...
javac -cp %LIB% -d %BIN% ^
  %SRC%\criminaldb\model\Criminal.java ^
  %SRC%\criminaldb\db\DBConnection.java ^
  %SRC%\criminaldb\db\CriminalDAO.java ^
  %SRC%\criminaldb\db\CaseDAO.java ^
  %SRC%\criminaldb\utils\UITheme.java ^
  %SRC%\criminaldb\ui\DashboardPanel.java ^
  %SRC%\criminaldb\ui\CriminalFormDialog.java ^
  %SRC%\criminaldb\ui\CriminalPanel.java ^
  %SRC%\criminaldb\ui\CrimePanel.java ^
  %SRC%\criminaldb\ui\AnalyticsPanel.java ^
  %SRC%\criminaldb\ui\FaceRecPanel.java ^
  %SRC%\criminaldb\ui\MainFrame.java

if %errorlevel% neq 0 (
    echo [ERROR] Compilation failed.
    pause
    exit /b 1
)

echo [2/3] Compilation successful!
echo [3/3] Launching application...

java -cp "%BIN%;%LIB%" criminaldb.ui.MainFrame

pause
