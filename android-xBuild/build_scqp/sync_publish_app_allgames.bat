@echo off
set base_dir=%~dp0

pushd %base_dir%

set PACKAGE_STYLE=%1

REM copy res in current directory, including scripts, images and audio
if YES==%CONFIG% goto run



call sync_config.bat

:run

call sync_clear.bat
call sync_res.bat
call sync_compile.bat

call sync_publish_game_hall.bat

call sync_publish_main.bat
call sync_publish_zip.bat

set assetZipDir=%ANDROID_RES_PATH%\games\
if exist %assetZipDir% rd %assetZipDir% /S /Q
if not exist %assetZipDir% mkdir %assetZipDir%
xcopy %ANDROID_PATH%\gameZips\hall.zip %assetZipDir% 

exit