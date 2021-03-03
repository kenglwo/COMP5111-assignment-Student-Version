#!/bin/bash

DIR=$( cd "$(dirname "$0")" >/dev/null 2>&1 || exit ; pwd -P )
ROOT_DIR="$DIR"/..
cd "$DIR"/.. || exit

# returns the JDK version.
# 8 for 1.8.0_nn, 9 for 9-ea etc, and "no_java" for undetected
jdk_version() {
  local result
  local java_cmd
  if [[ -n $(type -p java) ]]
  then
    java_cmd=java
  elif [[ (-n "$JAVA_HOME") && (-x "$JAVA_HOME/bin/java") ]]
  then
    java_cmd="$JAVA_HOME/bin/java"
  fi
  local IFS=$'\n'
  # remove \r for Cygwin
  local lines=$("$java_cmd" -Xms32M -Xmx32M -version 2>&1 | tr '\r' '\n')
  if [[ -z $java_cmd ]]
  then
    result=no_java
  else
    for line in $lines; do
      if [[ (-z $result) && ($line = *"version \""*) ]]
      then
        local ver=$(echo $line | sed -e 's/.*version "\(.*\)"\(.*\)/\1/; 1q')
        # on macOS, sed doesn't support '?'
        if [[ $ver = "1."* ]]
        then
          result=$(echo $ver | sed -e 's/1\.\([0-9]*\)\(.*\)/\1/; 1q')
        else
          result=$(echo $ver | sed -e 's/\([0-9]*\)\(.*\)/\1/; 1q')
        fi
      fi
    done
  fi
  echo "$result"
}
if [ "$(jdk_version)" != 8 ]; then
  echo "current jdk version is $(jdk_version), not using java 8"
  exit 1
fi

if [ ! -d "$ROOT_DIR"/raw-classes ]; then
    mkdir -p "$ROOT_DIR"/raw-classes
fi
# compile class under test
echo "compiling comp5111.assignment.cut ..."
# comp5111.assignment.cut
# javac -d "$ROOT_DIR"/raw-classes "$ROOT_DIR"/src/main/java/castle/comp5111/example/Subject.java
javac -d "$ROOT_DIR"/raw-classes "$ROOT_DIR"/src/main/java/comp5111/assignment/cut/ToolBox.java

# test generation using randoop
# We set the output directory to maven test source folder
# java -classpath .:"$ROOT_DIR"/raw-classes:"$ROOT_DIR"/lib/randoop-all-4.12.jar randoop.main.Main \
#   gentests --testclass castle.comp5111.example.Subject --output-limit 50 \
#   --junit-output-dir "$DIR"/../src/main/java --junit-package-name castle.comp5111.example.test
java -classpath .:"$ROOT_DIR"/raw-classes:"$ROOT_DIR"/lib/randoop-all-4.2.5.jar randoop.main.Main \
  gentests --testclass comp5111.assignment.cut.ToolBox --output-limit 60 \
	--junit-package-name comp5111.assignment.cut \
	--junit-output-dir=${ROOT_DIR}/target/classes \
	--randomseed=6

