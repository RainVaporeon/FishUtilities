@echo off
if "%1" == "" (
echo USAGE: push ^<version^> [-snapshot]
echo pushes current FishUtilities build
) else (
set version=%1
if "%2" == "-snapshot" (
set version=%1-SNAPSHOT
)
echo pushing %version%...
mvn install:install-file -Dfile="%cd%\build\libs\FishUtilities-%version%.jar" -DgroupId=io.github.rainvaporeon.fishutils -DartifactId=FishUtilities -Dversion=%version% -Dpackaging=jar -DgeneratePom=true
echo pushed.
)