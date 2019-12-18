#!/bin/bash
# Call the file as such: 
#   ./run-parallel.sh /path/to/java
# a single parameter should be passed in to the java v8 command line tool.
# My path to java in osx is /Library/Java/JavaVirtualMachines/adoptopenjdk-8.jdk/Contents/Home/bin/

# Clean all class files
rm Parallel/*.class Utils/*.class Kruskals/*.class *.class

# Build files
JAVAAGENT="-javaagent:./lib/hjlib-cooperative-0.1.4-SNAPSHOT.jar"
CLASSPATH=".:./lib/hjlib-cooperative-0.1.4-SNAPSHOT.jar:./lib/asm-all-5.0.3.jar:./lib/commons-math3-3.6.1.jar"
BUILD_JAVA="${1}/javac -classpath ${CLASSPATH}"
RUN_JAVA="${1}/java ${JAVAAGENT} -classpath ${CLASSPATH}"

$BUILD_JAVA Parallel/*.java Utils/*.java Kruskals/*.java *.java

# Run all Utilities Kruskals and Helper classes, Their main file should have a Test call.
$RUN_JAVA Main
