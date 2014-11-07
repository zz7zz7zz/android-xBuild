@echo off
if YES==%CONFIG% goto run

call sync_config.bat

:run

set files_zips=%ANDROID_PATH%\gameZips\

set filesDir_ddz=%ANDROID_PATH%\game_ddz\
set filesDir_scddz=%ANDROID_PATH%\game_scddz\
set filesDir_scmj=%ANDROID_PATH%\game_scmj\
set filesDir_erren=%ANDROID_PATH%\game_erren\
set filesDir_eqs=%ANDROID_PATH%\game_eqs\
set filesDir_magu=%ANDROID_PATH%\game_magu\
set filesDir_xlch=%ANDROID_PATH%\game_xlch\
set filesDir_hall=%ANDROID_PATH%\game_hall\

if exist %files_zips% rd %files_zips% /S /Q
if not exist %files_zips% mkdir %files_zips%

%TOOL_7ZIP_DIR% a -tzip %files_zips%\ddz.zip %filesDir_ddz%\*
%TOOL_7ZIP_DIR% a -tzip %files_zips%\scddz.zip %filesDir_scddz%\*
%TOOL_7ZIP_DIR% a -tzip %files_zips%\scmj.zip %filesDir_scmj%\*
%TOOL_7ZIP_DIR% a -tzip %files_zips%\erren.zip %filesDir_erren%*
%TOOL_7ZIP_DIR% a -tzip %files_zips%\eqs.zip %filesDir_eqs%*
%TOOL_7ZIP_DIR% a -tzip %files_zips%\magu.zip %filesDir_magu%*
%TOOL_7ZIP_DIR% a -tzip %files_zips%\xlch.zip %filesDir_xlch%*
%TOOL_7ZIP_DIR% a -tzip %files_zips%\hall.zip %filesDir_hall%*