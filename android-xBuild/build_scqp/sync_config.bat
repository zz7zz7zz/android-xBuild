set base_dir=%~dp0

pushd %base_dir%

set CONFIG=YES

REM PLATFORM CONTAIN android/ios/wp
set AUDIO_FORMAT=ogg
set AUDIO_UPDATE_ROOT=update

echo on
if ios==%PACKAGE_STYLE% (
	set AUDIO_FORMAT=mp3
	set AUDIO_UPDATE_ROOT=user
) else (
	set AUDIO_FORMAT=ogg
	set AUDIO_UPDATE_ROOT=update
)

set ANDROID_PATH=%PROJECT_ANDROID_PATH%
set ANDROID_RES_PATH=%ANDROID_PATH%\assets
set ANDROID_IMAGE_PATH=%ANDROID_RES_PATH%\images
set ANDROID_SCRIPTS_PATH=%ANDROID_RES_PATH%\scripts
set ANDROID_FONT_PATH=%ANDROID_RES_PATH%\fonts
set ANDROID_AUDIO_PATH=%ANDROID_RES_PATH%\audio
set ANDROID_AUDIO_OGG_PATH=%ANDROID_RES_PATH%\audio\%AUDIO_FORMAT%

set LUA_PATH=%PROJECT_LUA_PATH%
set LUA_IMAGES_PATH=%LUA_PATH%\images
set LUA_SCRIPTS_PATH=%LUA_PATH%\scripts
set LUA_FONT_PATH=%LUA_PATH%\font
set LUA_AUDIO_OGG_PATH=%LUA_PATH%\audio\%AUDIO_FORMAT%

set TOOL_LUAC=%cd%\compile\luac.exe
set TOOL_CONVERT=%cd%\compile\convert\convert.exe
set TOOL_ENCODE=%cd%\compile\BinaryEncoder.exe
set TOOL_7ZIP_DIR=%cd%\7-Zip\7z.exe