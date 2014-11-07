@echo off
if YES==%CONFIG% goto run

call sync_config.bat

:run

REM delete res in current directory, including scripts, images and audio
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
echo @pause

REM copy res in current directory, including scripts, images and audio

if not exist %ANDROID_IMAGE_PATH% mkdir %ANDROID_IMAGE_PATH%
if not exist %ANDROID_SCRIPTS_PATH% mkdir %ANDROID_SCRIPTS_PATH%
if not exist %ANDROID_FONT_PATH% mkdir %ANDROID_FONT_PATH%
if not exist %ANDROID_AUDIO_OGG_PATH% mkdir %ANDROID_AUDIO_OGG_PATH%

echo copy images ...
xcopy %IMAGES_PATH%.\*.* %ANDROID_IMAGE_PATH% /e /s /Y

echo copy scripts ...
xcopy %SCRIPTS_PATH%.\*.* %ANDROID_SCRIPTS_PATH% /e /s /Y

echo copy fonts ...
if exist %FONT_PATH% xcopy %FONT_PATH%.\*.*	%ANDROID_FONT_PATH% /e /s /Y

echo copy audio
xcopy %AUDIO_OGG_PATH%.\*.* %ANDROID_AUDIO_OGG_PATH% /e /s /Y

echo end xcopy ...