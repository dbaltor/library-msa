#!/bin/sh
set -eo pipefail

cd $(dirname $0)
../sc-6-tracing/scripts/build.sh
