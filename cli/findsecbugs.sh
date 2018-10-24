#!/bin/bash

cd $(dirname $(readlink `[[ $OSTYPE == linux* ]] && echo "-f"` $0))

java -cp lib/\* edu.umd.cs.findbugs.LaunchAppropriateUI -quiet -pluginList lib/findsecbugs-plugin-1.8.0.jar -include include.xml $@
