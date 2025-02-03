if [ -z "$1" ]; then
    echo 'USAGE: push <version> [-s]'
    echo 'pushes current FishUtilities build'
    exit 1
fi

version=$1;

if [ "$2" == "-s" ]; then
    version="$1-SNAPSHOT"
fi

jarfile="./build/libs/FishUtilities-$version.jar"

if [ ! -f "$jarfile" ]; then
    echo "$jarfile not found. Run ./gradlew build first."
    exit 1
fi

echo "pushing $version..."
mvn install:install-file -Dfile="$jarfile" -DgroupId=io.github.rainvaporeon.fishutils -DartifactId=FishUtilities -Dversion="$version" -Dpackaging=jar -DgeneratePom=true
echo "pushed."
