@echo off

set /p "inpv=Mobi will be created, are you sure?"


if /i "%inpv%" neq "Y" goto EX


set JAVA_HOME=C:\Program Files\Java\jdk1.8.0_25
set PATH=%JAVA_HOME%\bin;%PATH%

set ANT_HOME=D:\ProgramFiles\eclipse\apache-ant-1.9.4
set PATH=%ANT_HOME%\bin;%PATH%


E:
cd %~dp0\..

::copy config files
copy build\BuildConfig\build_renzhongziben.xml build.xml   /y


::update versionCode and versionName
build\Other\AndroidBuildHelper.jar -fAndroidManifestFiles\AndroidManifest_RenZhongZiBen.xml

copy AndroidManifestFiles\AndroidManifest_RenZhongZiBen.xml AndroidManifest.xml /y


::delete bin and gen 
rd /S /Q bin
::rd /S /Q gen




::create apk file
call ant release -buildfile build.xml preparefiles


::copy apk to E:\Temp
copy %~dp0\..\bin\WestSoft_Common-release.apk E:\Users\Administrator\Desktop\rzzb.apk /y


:EX
echo exiting...