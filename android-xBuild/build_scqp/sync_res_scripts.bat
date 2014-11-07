REM copy res in current directory, including scripts, images and audio
@echo off

if YES==%CONFIG% goto run

call sync_config.bat

:run

echo delete scripts start...

if exist %ANDROID_SCRIPTS_PATH% rd %ANDROID_SCRIPTS_PATH% /S /Q
if exist %ANDROID_SCRIPTS_PATH% rd %ANDROID_SCRIPTS_PATH% 

echo delete scripts completed

if not exist %ANDROID_SCRIPTS_PATH% mkdir %ANDROID_SCRIPTS_PATH%

echo copy scripts ...

xcopy %SCRIPTS_PATH%.\*.* %ANDROID_SCRIPTS_PATH% /e /s /Y

echo end xcopy ...