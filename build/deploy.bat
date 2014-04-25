@echo off
pushd "%cd%" && cd /d "%~dp0" && ( if exist "%temp%\getadmin.vbs" del "%temp%\getadmin.vbs" ) && fsutil dirty query %systemdrive% >nul || if %errorlevel%==0 ( echo Set UAC = CreateObject^("Shell.Application"^) > "%temp%\getadmin.vbs" && echo UAC.ShellExecute "%~s0", "", "", "runas", 1 >> "%temp%\getadmin.vbs" && "%temp%\getadmin.vbs" && exit /B )
cd .. && cd ..
runtime\bin\python\python_mcp src-mods\build\runtime\deploy.py %*