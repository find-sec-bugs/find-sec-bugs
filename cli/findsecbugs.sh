
SOURCE="${BASH_SOURCE[0]}"

java -cp "$SOURCE/lib/\*:" edu.umd.cs.findbugs.LaunchAppropriateUI -quiet -pluginList lib/findsecbugs-plugin-1.7.1.jar -include include.xml $@
