#!/bin/bash
# Build and then run the project
mvn clean package && java -jar target/IRPFDecomp-1.0-SNAPSHOT.jar
