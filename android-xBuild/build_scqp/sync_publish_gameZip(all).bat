@echo off
REM copy res in current directory, including scripts, images and audio
if YES==%CONFIG% goto run

call sync_config.bat

:run

call sync_clear.bat
call sync_res.bat
call sync_compile.bat

call sync_publish_game_hall.bat

call sync_publish_main.bat

set files_zips=%ANDROID_PATH%\gameZips\

if exist %files_zips% rd %files_zips% /S /Q
if not exist %files_zips% mkdir %files_zips%

set filesDir_hall=%ANDROID_PATH%\game_hall\
%TOOL_7ZIP_DIR% a -tzip %files_zips%\hall.zip %filesDir_hall%*