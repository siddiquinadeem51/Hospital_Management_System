@echo off
setlocal

if not exist out mkdir out

javac -d out src\hospital\*.java
if errorlevel 1 (
    echo Compilation failed.
    exit /b 1
)

java -cp out hospital.Main
