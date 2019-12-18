#!/bin/bash
# Clean all class files
rm Utils/*.class Kruskals/*.class *.class

# Build and run the projects
CLASSPATH=".:./lib/commons-math3-3.6.1.jar"
BUILD_JAVA="javac -classpath ${CLASSPATH}"
RUN_JAVA="java -Xms12g -Xmx12g -Xmx12g -classpath ${CLASSPATH}"

$BUILD_JAVA Utils/*.java Kruskals/*.java *.java

# Clean up folders
rm -rf RawScore
rm -rf SizeVSMean
rm -rf SizeVSStd
rm -rf ZScore
rm -rf Edges
mkdir RawScore
mkdir SizeVSMean
mkdir SizeVSStd
mkdir ZScore
mkdir Edges

$RUN_JAVA Main