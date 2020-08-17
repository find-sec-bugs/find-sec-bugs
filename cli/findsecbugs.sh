#!/bin/bash

SOURCE="${BASH_SOURCE[0]}"
while [ -h "$SOURCE" ]; do # resolve $SOURCE until the file is no longer a symlink
  DIR="$( cd -P "$( dirname "$SOURCE" )" >/dev/null 2>&1 && pwd )"
  SOURCE="$(readlink "$SOURCE")"
  [[ $SOURCE != /* ]] && SOURCE="$DIR/$SOURCE" # if $SOURCE was a relative symlink, we need to resolve it relative to the path where the symlink file was located
done
DIR="$( cd -P "$( dirname "$SOURCE" )" >/dev/null 2>&1 && pwd )"
FINDBUGS_PLUGIN="$(find "$DIR"/lib/findsecbugs-plugin-* | sort -V | tail -n1)"

for LIB in "$DIR"/lib/*.jar; do
  if [[ -z "${LIBS// }" ]]; then
    LIBS=$LIB
  else
    LIBS=$LIB:$LIBS
  fi
done

java -cp "$LIBS" edu.umd.cs.findbugs.LaunchAppropriateUI -quiet -pluginList "$FINDBUGS_PLUGIN" -include "$DIR"/include.xml $@
