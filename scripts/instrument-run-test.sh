#!/bin/bash

DIR=$( cd "$(dirname "$0")" >/dev/null 2>&1 || exit ; pwd -P )
ROOT_DIR="$DIR"/..
cd "$ROOT_DIR" || exit

if [ ! -d "$ROOT_DIR"/raw-classes ]; then
    mkdir -p "$ROOT_DIR"/raw-classes
fi

# 1. we compile the class under test castle.comp5111.example.Subject
echo "compiling comp5111.assignment.cut ..."
javac -d "$ROOT_DIR"/raw-classes "$ROOT_DIR"/src/main/java/comp5111/assignment/cut/ToolBox.java


# 2. we compile the classes to instrument Subject and count invocations using soot
echo "compiling instrumentation classes ..."
if [ ! -d "$ROOT_DIR"/target/classes ]; then
    mkdir -p "$ROOT_DIR"/target/classes
fi
find "$ROOT_DIR"/src/main/java -name "*.java" -print0 | xargs -0 \
  javac -g -classpath .:"$ROOT_DIR"/lib/soot-4.2.1-jar-with-dependencies.jar:"$ROOT_DIR"/lib/junit-4.12.jar -d "$ROOT_DIR"/target/classes

find "$ROOT_DIR"/src/test/randoop0 -name "*.java" -print0 | xargs -0 \
  javac -g -classpath .:"$ROOT_DIR"/lib/soot-4.2.1-jar-with-dependencies.jar:"$ROOT_DIR"/lib/junit-4.12.jar:"$ROOT_DIR"/src/main/java -d "$ROOT_DIR"/target/classes/randoop0
find "$ROOT_DIR"/src/test/randoop1 -name "*.java" -print0 | xargs -0 \
  javac -g -classpath .:"$ROOT_DIR"/lib/soot-4.2.1-jar-with-dependencies.jar:"$ROOT_DIR"/lib/junit-4.12.jar:"$ROOT_DIR"/src/main/java -d "$ROOT_DIR"/target/classes/randoop1
find "$ROOT_DIR"/src/test/randoop2 -name "*.java" -print0 | xargs -0 \
  javac -g -classpath .:"$ROOT_DIR"/lib/soot-4.2.1-jar-with-dependencies.jar:"$ROOT_DIR"/lib/junit-4.12.jar:"$ROOT_DIR"/src/main/java -d "$ROOT_DIR"/target/classes/randoop2
find "$ROOT_DIR"/src/test/randoop3 -name "*.java" -print0 | xargs -0 \
  javac -g -classpath .:"$ROOT_DIR"/lib/soot-4.2.1-jar-with-dependencies.jar:"$ROOT_DIR"/lib/junit-4.12.jar:"$ROOT_DIR"/src/main/java -d "$ROOT_DIR"/target/classes/randoop3
find "$ROOT_DIR"/src/test/randoop4 -name "*.java" -print0 | xargs -0 \
  javac -g -classpath .:"$ROOT_DIR"/lib/soot-4.2.1-jar-with-dependencies.jar:"$ROOT_DIR"/lib/junit-4.12.jar:"$ROOT_DIR"/src/main/java -d "$ROOT_DIR"/target/classes/randoop4

# 3. we run the main method of castle.comp5111.example.EntryPoint

# ====== statement coverage
java -cp .:lib/soot-4.2.1-jar-with-dependencies.jar:lib/junit-4.12.jar:lib/hamcrest-core-1.3.jar:target/classes comp5111.assignment.Assignment1 0 ToolBox 'ToolBox$ArrayTools' 'ToolBox$CharSequenceTools' 'ToolBox$CharTools' 'ToolBox$LocaleTools' 'ToolBox$RegExTools' 'ToolBox$StringTools'

# java -cp .:lib/soot-4.2.1-jar-with-dependencies.jar:lib/junit-4.12.jar:lib/hamcrest-core-1.3.jar:target/classes comp5111.assignment.Assignment1 0 ToolBox 
#
# java -cp .:lib/soot-4.2.1-jar-with-dependencies.jar:lib/junit-4.12.jar:lib/hamcrest-core-1.3.jar:target/classes comp5111.assignment.Assignment1 0 'ToolBox$ArrayTools' 
#
# java -cp .:lib/soot-4.2.1-jar-with-dependencies.jar:lib/junit-4.12.jar:lib/hamcrest-core-1.3.jar:target/classes comp5111.assignment.Assignment1 0  'ToolBox$CharSequenceTools'
#
# java -cp .:lib/soot-4.2.1-jar-with-dependencies.jar:lib/junit-4.12.jar:lib/hamcrest-core-1.3.jar:target/classes comp5111.assignment.Assignment1 0 'ToolBox$CharTools' 
#
# java -cp .:lib/soot-4.2.1-jar-with-dependencies.jar:lib/junit-4.12.jar:lib/hamcrest-core-1.3.jar:target/classes comp5111.assignment.Assignment1 0 'ToolBox$LocaleTools' 
#
# java -cp .:lib/soot-4.2.1-jar-with-dependencies.jar:lib/junit-4.12.jar:lib/hamcrest-core-1.3.jar:target/classes comp5111.assignment.Assignment1 0 'ToolBox$RegExTools' 'ToolBox$StringTools'
#
# java -cp .:lib/soot-4.2.1-jar-with-dependencies.jar:lib/junit-4.12.jar:lib/hamcrest-core-1.3.jar:target/classes comp5111.assignment.Assignment1 0  'ToolBox$StringTools'

java -cp .:lib/soot-4.2.1-jar-with-dependencies.jar:lib/junit-4.12.jar:lib/hamcrest-core-1.3.jar:target/classes comp5111.assignment.Assignment1 1 ToolBox 'ToolBox$ArrayTools' 'ToolBox$CharSequenceTools' 'ToolBox$CharTools' 'ToolBox$LocaleTools' 'ToolBox$RegExTools' 'ToolBox$StringTools'


