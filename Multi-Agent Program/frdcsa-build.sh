#!/bin/bash

export JAVA_HOME=/usr/lib/jvm/adoptopenjdk-16-hotspot-amd64
export JDK_HOME=/usr/lib/jvm/adoptopenjdk-16-hotspot-amd64
export JAVA=/usr/lib/jvm/adoptopenjdk-16-hotspot-amd64/bin/java
export CLASSPATH=/usr/lib/jvm/adoptopenjdk-16-hotspot-jre-amd64/lib

export JASON_HOME=/var/lib/myfrdcsa/sandbox/jason-20211129/jason-20211129/build

./gradlew clean
./gradlew config
# ./gradlew doc
./gradlew jar
# ./gradlew release
