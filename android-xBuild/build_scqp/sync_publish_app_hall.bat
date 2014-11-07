@echo off
set base_dir=%~dp0

pushd %base_dir%

@echo off
REM copy res in current directory, including scripts, images and audio
if YES==%CONFIG% goto run

set PACKAGE_STYLE=%1

call sync_config.bat

:run

call sync_clear.bat
call sync_res.bat
call sync_compile.bat


call sync_publish_game_scmj.bat
call sync_publish_game_erren.bat
call sync_publish_game_ddz.bat
call sync_publish_game_scddz.bat
call sync_publish_game_magu.bat
call sync_publish_game_eqs.bat
call sync_publish_game_xlch.bat

call sync_publish_game_hall.bat

call sync_publish_main.bat
call sync_publish_zip.bat

set assetZipDir=%ANDROID_RES_PATH%\games\
if exist %assetZipDir% rd %assetZipDir% /S /Q
if not exist %assetZipDir% mkdir %assetZipDir%
xcopy %ANDROID_PATH%\gameZips\hall.zip %assetZipDir% 

exit