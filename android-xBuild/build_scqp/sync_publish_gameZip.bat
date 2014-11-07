@echo off
set base_dir=%~dp0

pushd %base_dir%

REM copy res in current directory, including scripts, images and audio
if YES==%CONFIG% goto run

@echo on

set PACKAGE_STYLE=%1
set PROJECT_PATH=%2
set para=%*

echo PACKAGE_STYLE=%PACKAGE_STYLE%
echo PROJECT_PATH=%PROJECT_PATH%
echo para=%para%


call sync_config.bat

:run

call sync_clear.bat
call sync_res.bat
call sync_compile.bat

for %%i in (%para%) do (
if %%i==%PACKAGE_STYLE% (
	echo ---A:--%%i-----
) else if %%i ==%PROJECT_PATH% (
	echo ---B:--%%i-----
) else (
	echo ---C:--%%i-----
	call sync_publish_game.bat %%i
))

call sync_publish_game_hall.bat

call sync_publish_main.bat
call sync_publish_zip.bat

exit

