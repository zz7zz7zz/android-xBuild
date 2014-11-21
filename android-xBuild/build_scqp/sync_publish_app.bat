@echo off
set base_dir=%~dp0

pushd %base_dir%

REM copy res in current directory, including scripts, images and audio
if YES==%CONFIG% goto run

set PACKAGE_STYLE=%1
set PROJECT_ANDROID_PATH=%2
set PROJECT_LUA_PATH=%3
set para=%*

echo PACKAGE_STYLE=%PACKAGE_STYLE%
echo PROJECT_ANDROID_PATH=%PROJECT_ANDROID_PATH%
echo PROJECT_LUA_PATH=%PROJECT_LUA_PATH%
echo para=%para%

call sync_config.bat

:run

call sync_clear.bat
call sync_res.bat
call sync_compile.bat

for %%i in (%para%) do (
if %%i==%PACKAGE_STYLE% (
	echo ---A:--%%i-----
) else if %%i ==%PROJECT_ANDROID_PATH% (
	echo ---B:--%%i-----
) else if %%i ==%PROJECT_LUA_PATH% (
	echo ---C:--%%i-----
) else (
	echo ---D:--%%i-----
	call sync_publish_game.bat %%i
))

call sync_publish_game_hall.bat
call sync_publish_main.bat
call sync_publish_zip.bat

set assetZipDir=%ANDROID_RES_PATH%\games\
if exist %assetZipDir% rd %assetZipDir% /S /Q
if not exist %assetZipDir% mkdir %assetZipDir%
xcopy %ANDROID_PATH%\gameZips\hall.zip %assetZipDir%

exit