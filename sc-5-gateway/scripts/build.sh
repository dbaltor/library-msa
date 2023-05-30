#!/bin/sh
set -eo pipefail

cd $(dirname $0)
cd ..
../gradlew reader:publishToMavenLocal
../gradlew book:publishToMavenLocal
../gradlew clean build --parallel
