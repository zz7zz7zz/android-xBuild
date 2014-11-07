REM copy res in current directory, including scripts, images and audio
@echo off

if YES==%CONFIG% goto run

call sync_config.bat

:run

echo delete scripts start...

if exist %ANDROID_IMAGE_PATH% rd %ANDROID_IMAGE_PATH% /S /Q
if exist %ANDROID_IMAGE_PATH% rd %ANDROID_IMAGE_PATH% 

echo delete scripts completed

if not exist %ANDROID_IMAGE_PATH% mkdir %ANDROID_IMAGE_PATH%

echo copy scripts ...

xcopy %IMAGES_PATH%.\*.* %ANDROID_IMAGE_PATH% /e /s /Y

echo end xcopy ...