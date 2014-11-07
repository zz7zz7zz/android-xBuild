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

set ANDROID_PATH=%PROJECT_PATH%
set ANDROID_RES_PATH=%ANDROID_PATH%\assets
set ANDROID_IMAGE_PATH=%ANDROID_RES_PATH%\images
set ANDROID_SCRIPTS_PATH=%ANDROID_RES_PATH%\scripts
set ANDROID_FONT_PATH=%ANDROID_RES_PATH%\fonts
set ANDROID_AUDIO_PATH=%ANDROID_RES_PATH%\audio
set ANDROID_AUDIO_OGG_PATH=%ANDROID_RES_PATH%\audio\%AUDIO_FORMAT%

set IMAGES_PATH=%ANDROID_PATH%\..\..\Resource\images
set SCRIPTS_PATH=%ANDROID_PATH%\..\..\Resource\scripts
set FONT_PATH=%ANDROID_PATH%\..\..\Resource\font
set AUDIO_OGG_PATH=%ANDROID_PATH%\..\..\Resource\audio\%AUDIO_FORMAT%



set TOOL_LUAC=%ANDROID_PATH%\..\..\tools\luac.exe
set TOOL_CONVERT=%ANDROID_PATH%\..\..\tools\convert\convert.exe
SET TOOL_ENCODE=%ANDROID_PATH%\..\..\tools\BinaryEncoder.exe
SET TOOL_7ZIP_DIR=%ANDROID_PATH%\7-Zip\7z.exe
