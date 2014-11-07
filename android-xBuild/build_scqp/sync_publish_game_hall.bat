@echo off
if YES==%CONFIG% goto run

call sync_config.bat

:run

REM set hall A ----------= directory, including scripts, images and audio

set hall_RES_PATH=%ANDROID_PATH%\game_hall\%AUDIO_UPDATE_ROOT%
set hall_IMAGE_PATH=%hall_RES_PATH%\images\
set hall_AUDIO_PATH=%hall_RES_PATH%\audio\%AUDIO_FORMAT%\
set hall_SCRIPTS_PATH=%hall_RES_PATH%\scripts\


REM delete hall B ---------- in current directory, including scripts, images and audio
echo delete all path start...

if exist %ANDROID_PATH%\game_hall rd %ANDROID_PATH%\game_hall /S /Q
if exist %ANDROID_PATH%\game_hall rd %ANDROID_PATH%\game_hall 

echo delete all path completed
echo @pause

REM copy hall C ---------- in current directory, including scripts, images and audio

if not exist %hall_IMAGE_PATH% mkdir %hall_IMAGE_PATH%
if not exist %hall_AUDIO_PATH% mkdir %hall_AUDIO_PATH%
if not exist %hall_SCRIPTS_PATH% mkdir %hall_SCRIPTS_PATH%


echo copy images ...
xcopy %ANDROID_IMAGE_PATH%.\*.* %hall_IMAGE_PATH% /e /s /Y

echo copy audio
xcopy %ANDROID_AUDIO_OGG_PATH%.\*.* %hall_AUDIO_PATH% /e /s /Y

echo copy scripts ...
xcopy %ANDROID_SCRIPTS_PATH%.\*.* %hall_SCRIPTS_PATH% /e /s /Y

echo end xcopy ...

REM delete hall D ---------- in current directory, including scripts, images and audio
if exist %ANDROID_IMAGE_PATH% rd %ANDROID_IMAGE_PATH% /S /Q
if exist %ANDROID_AUDIO_PATH% rd %ANDROID_AUDIO_PATH% /S /Q
if exist %ANDROID_FONT_PATH% rd %ANDROID_FONT_PATH% /S /Q
if exist %ANDROID_SCRIPTS_PATH% rd %ANDROID_SCRIPTS_PATH% /S /Q

