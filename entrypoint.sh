#!/bin/sh

java -jar -Dspring.profiles.active=testEnv dbtool-1.0.0.jar fC fM eC eM eI
java -jar -Dspring.profiles.active=deploymentEnv outbox-1.0.0.jar
#java -jar -Dspring.profiles.active=deploymentEnv iamoutbox-1.0.0.jar
#java -jar -Dspring.profiles.active=deploymentEnv iam-1.0.0.jar