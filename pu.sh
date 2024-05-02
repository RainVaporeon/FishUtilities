if [ "$1" == "" ]; then
echo 'USAGE: push \<version\> \[-snapshot\]'
echo 'pushes current FishUtilities build;'
else
version=$1;
esle
fi

if [ "$2" == "-snapshot" ]; then
version="$1-SNAPSHOT"
fi
echo "pushing $version..."
mvn install:install-file -Dfile=".\build\libs\FishUtilities-$version.jar" -DgroupId=io.github.rainvaporeon.fishutils -DartifactId=FishUtilities -Dversion=%version% -Dpackaging=jar -DgeneratePom=true
echo pushed.
