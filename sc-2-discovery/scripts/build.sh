#!/bin/bash
set -eo pipefail

cd $(dirname $0)
cd ..
../gradlew clean build --parallel
