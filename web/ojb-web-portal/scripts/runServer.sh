#!/bin/bash

pushd .. >> /dev/null

echo "Compiling...."
mvn package -Dmaven.test.skip=true  >> /dev/null

echo "Starting Tomcat"
mvn t7:run  -Dserver.waitOnStartup=true

popd >> /dev/null
