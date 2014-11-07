@echo off

if YES==%CONFIG% goto run

call sync_config.bat

:run

@echo ============================
@echo start compile lua

echo start compile

for /r %%i in (*.lua) do (
	echo %%i
	pushd %%~dpi\
	%TOOL_LUAC% -o %%~nxi %%~nxi
	popd
)

popd
@echo end compile lua

@echo start copy main.lua error.lua

copy %SCRIPTS_PATH%\main.lua %ANDROID_RES_PATH%\scripts\
copy %SCRIPTS_PATH%\error.lua %ANDROID_RES_PATH%\scripts\

@echo end