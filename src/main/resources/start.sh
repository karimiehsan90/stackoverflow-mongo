#!/usr/bin/env bash

set -o errexit

BASEDIR=$(dirname $0)/..
APPNAME="spring-boot-mongodb"
VERSION="0.0.1-SNAPSHOT"

cd "$BASEDIR"
jar uf "lib/$APPNAME-$VERSION.jar" -C conf/ .
mkdir -p logs
cd logs
java -jar "../lib/$APPNAME-$VERSION.jar"
