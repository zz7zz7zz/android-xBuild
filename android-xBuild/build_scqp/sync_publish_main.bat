@echo off
if YES==%CONFIG% goto run

call sync_config.bat

:run

REM set main A ----------= directory, including scripts, images and audio

set hall_RES_PATH=%ANDROID_PATH%\game_hall\update
set hall_SCRIPTS_PATH=%hall_RES_PATH%\scripts\

set main_SCRIPTS_PATH=%ANDROID_SCRIPTS_PATH%


REM delete main B ---------- in current directory, including scripts, images and audio
echo delete all path start...

if exist %main_SCRIPTS_PATH% rd %main_SCRIPTS_PATH% /S /Q

echo delete all path completed
echo @pause

REM copy main C ---------- in current directory, including scripts, images and audio


if not exist %main_SCRIPTS_PATH% mkdir %main_SCRIPTS_PATH%


echo start xcopy ...

echo copy scripts ...
xcopy %hall_SCRIPTS_PATH%.\error.lua %main_SCRIPTS_PATH% 
xcopy %hall_SCRIPTS_PATH%.\main.lua %main_SCRIPTS_PATH% 
xcopy %hall_SCRIPTS_PATH%.\mainHandle.lua %main_SCRIPTS_PATH% 
xcopy %hall_SCRIPTS_PATH%.\errorHandle.lua %main_SCRIPTS_PATH% 

echo end xcopy ...


