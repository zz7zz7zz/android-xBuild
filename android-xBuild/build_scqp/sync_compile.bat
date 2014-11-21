@echo off

if YES==%CONFIG% goto run

call sync_config.bat

:run

@echo ============================
@echo start compile lua

echo start compile

pushd %ANDROID_SCRIPTS_PATH%\..

for /r %%i in (*.lua) do (
	%TOOL_LUAC% -o %%~dpi\%%~nxi %%i 
)

popd

@echo end compile lua

@echo start copy main.lua error.lua

copy %LUA_SCRIPTS_PATH%\main.lua %ANDROID_RES_PATH%\scripts\
copy %LUA_SCRIPTS_PATH%\error.lua %ANDROID_RES_PATH%\scripts\

@echo end