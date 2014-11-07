@echo off
if YES==%CONFIG% goto run

call sync_config.bat

:run

@echo ----------------------------------------------------------------------------------------------------------------------%1-------------------------------------------------------------------------------------------------------------------------------------------------------

set gamePkg=%1

REM copy %gamePkg% 		A---------- res in current directory, including scripts, images and audio
set LuaGame_RES_PATH=%ANDROID_PATH%\game_%gamePkg%\%AUDIO_UPDATE_ROOT%
set LuaGame_IMAGE_PATH=%LuaGame_RES_PATH%\images\games\%gamePkg%
set LuaGame_AUDIO_PATH=%LuaGame_RES_PATH%\audio\%AUDIO_FORMAT%\games\%gamePkg%
set LuaGame_SCRIPTS_PATH=%LuaGame_RES_PATH%\scripts\games\%gamePkg%
set LuaGame_SCRIPTS_VIEW_PATH=%LuaGame_RES_PATH%\scripts\view\kScreen_1280_800\games\%gamePkg%


REM delete %gamePkg% 	B---------- in current directory, including scripts, images and audio
echo delete all path start...

if exist %ANDROID_PATH%\game_%gamePkg% rd %ANDROID_PATH%\game_%gamePkg% /S /Q
if exist %ANDROID_PATH%\game_%gamePkg% rd %ANDROID_PATH%\game_%gamePkg%

echo delete all path completed
echo @pause

REM copy %gamePkg% 		C---------- in current directory, including scripts, images and audio

if not exist %LuaGame_IMAGE_PATH% mkdir %LuaGame_IMAGE_PATH%
if not exist %LuaGame_AUDIO_PATH% mkdir %LuaGame_AUDIO_PATH%
if not exist %LuaGame_SCRIPTS_PATH% mkdir %LuaGame_SCRIPTS_PATH%
if not exist %LuaGame_SCRIPTS_VIEW_PATH% mkdir %LuaGame_SCRIPTS_VIEW_PATH%

echo copy images ...
xcopy %ANDROID_IMAGE_PATH%\games\%gamePkg%\.\*.* %LuaGame_IMAGE_PATH% /e /s /Y

echo copy audio
xcopy %ANDROID_AUDIO_OGG_PATH%\games\%gamePkg%\.\*.* %LuaGame_AUDIO_PATH% /e /s /Y

echo copy scripts ...
xcopy %ANDROID_SCRIPTS_PATH%\games\%gamePkg%\.\*.* %LuaGame_SCRIPTS_PATH% /e /s /Y

echo copy view
xcopy %ANDROID_SCRIPTS_PATH%\view\kScreen_1280_800\games\%gamePkg%\.\*.* %LuaGame_SCRIPTS_VIEW_PATH% /e /s /Y

echo end xcopy ...

REM delete %gamePkg% 	D ---------- in current directory, including scripts, images and audio

if exist %ANDROID_IMAGE_PATH%\games\%gamePkg% rd %ANDROID_IMAGE_PATH%\games\%gamePkg% /S /Q
if exist %ANDROID_AUDIO_OGG_PATH%\games\%gamePkg% rd %ANDROID_AUDIO_OGG_PATH%\games\%gamePkg% /S /Q
if exist %ANDROID_SCRIPTS_PATH%\games\%gamePkg% rd %ANDROID_SCRIPTS_PATH%\games\%gamePkg% /S /Q
if exist %ANDROID_SCRIPTS_PATH%\view\kScreen_1280_800\games\%gamePkg% rd %ANDROID_SCRIPTS_PATH%\view\kScreen_1280_800\games\%gamePkg% /S /Q
