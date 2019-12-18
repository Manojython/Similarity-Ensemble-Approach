#!/bin/bash
# Call the file as such: 
#   ./test.sh /path/to/java
# a single parameter should be passed in to the java v8 command line tool.
# My path in osx is /Library/Java/JavaVirtualMachines/adoptopenjdk-8.jdk/Contents/Home/bin/

# Clean all class files
rm Utils/*.class Kruskals/*.class *.class

# Build files
JAVAAGENT="-javaagent:./lib/hjlib-cooperative-0.1.4-SNAPSHOT.jar"
CLASSPATH=".:./lib/hjlib-cooperative-0.1.4-SNAPSHOT.jar:./lib/asm-all-5.0.3.jar:./lib/commons-math3-3.6.1.jar"
BUILD_JAVA="${1}/javac -classpath ${CLASSPATH}"
RUN_JAVA="${1}/java ${JAVAAGENT} -classpath ${CLASSPATH}"

$BUILD_JAVA Utils/*.java Kruskals/*.java *.java

# Run all Utilities Kruskals and Helper classes, Their main file should have a Test call.
$RUN_JAVA -ea Kruskals.Graph
$RUN_JAVA -ea Utils.Stats
$RUN_JAVA -ea Utils.Fitter
$RUN_JAVA -ea Utils.TimeStepPlot
$RUN_JAVA -ea Utils.Histogram
$RUN_JAVA -ea Sample
$RUN_JAVA -ea SampleFactorPair
$RUN_JAVA -ea RawScore
$RUN_JAVA -ea SampleProductFactors
$RUN_JAVA -ea ChemicalSimilaritySet
$RUN_JAVA -ea ZScore
$RUN_JAVA -ea EScore
