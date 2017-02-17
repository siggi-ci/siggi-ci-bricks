#!/bin/bash

mvn clean release:clean

mvn release:prepare --batch-mode

mvn release:perform -Darguments="-Dmaven.test.skip" --batch-mode
