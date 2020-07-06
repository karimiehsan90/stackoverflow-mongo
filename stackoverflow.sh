#!/usr/bin/env bash

set -o errexit

build() {
  mvn clean package -DskipTests

  docker build \
    -t test/stackoverflow \
    -f target/stackoverflow/Dockerfile \
    target/stackoverflow
}

parse-args() {
  METHOD=${1}
}

main() {
  parse-args "${@}"
  ${METHOD}
}

main "${@}"
