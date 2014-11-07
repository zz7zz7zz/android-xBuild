REM copy res in current directory, including scripts, images and audio
@echo off

if YES==%CONFIG% goto run

call sync_config.bat

:run

echo delete scripts start...

if exist %ANDROID_AUDIO_PATH% rd %ANDROID_AUDIO_PATH% /S /Q
if exist %ANDROID_AUDIO_PATH% rd %ANDROID_AUDIO_PATH% 

echo delete scripts completed

if not exist %ANDROID_AUDIO_OGG_PATH% mkdir %ANDROID_AUDIO_OGG_PATH%

echo copy scripts ...

xcopy %AUDIO_OGG_PATH%.\*.* %ANDROID_AUDIO_OGG_PATH% /e /s /Y

echo end xcopy ...