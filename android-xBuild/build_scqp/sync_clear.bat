@echo off

if YES==%CONFIG% goto run

call sync_config.bat

:run

echo delete all path start...

if exist %ANDROID_AUDIO_PATH% rd %ANDROID_AUDIO_PATH% /S /Q
if exist %ANDROID_IMAGE_PATH% rd %ANDROID_IMAGE_PATH% /S /Q
if exist %ANDROID_SCRIPTS_PATH% rd %ANDROID_SCRIPTS_PATH% /S /Q
if exist %ANDROID_FONT_PATH% rd %ANDROID_FONT_PATH% /S /Q

if exist %ANDROID_AUDIO_PATH% rd %ANDROID_AUDIO_PATH%
if exist %ANDROID_IMAGE_PATH% rd %ANDROID_IMAGE_PATH%
if exist %ANDROID_SCRIPTS_PATH% rd %ANDROID_SCRIPTS_PATH%
if exist %ANDROID_FONT_PATH% rd %ANDROID_FONT_PATH%

echo delete all path completed