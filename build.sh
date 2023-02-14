# bin/bash
./gradlew clean build
docker build -t modiconme/smartix:latest .
docker login -u $1 -p $2
docker push modiconme/smartix:latest
