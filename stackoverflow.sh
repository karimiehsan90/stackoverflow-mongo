#!/usr/bin/env bash

set -o errexit

CONTAINER_NAME=stackoverflow
IMAGE_NAME=test/stackoverflow
ACCEPTANCE_TEST_CONTAINER_NAME=acceptance-test
ACCEPTANCE_TEST_IMAGE_NAME=test/acceptance-test
MONGO_CONTAINER_NAME=mongo
MONGO_IMAGE_NAME=m.docker-registry.ir/mongo:3.6.3

build() {
  mvn clean package -DskipTests

  docker build \
    -t "${ACCEPTANCE_TEST_IMAGE_NAME}" \
    -f acceptance-test/Dockerfile \
    acceptance-test

  docker build \
    -t "${IMAGE_NAME}" \
    -f target/stackoverflow/Dockerfile \
    target/stackoverflow
}

prepare-env() {
  docker rm -f "${CONTAINER_NAME}" > /dev/null 2>&1 || \
    echo "There is no docker container named ${CONTAINER_NAME}"
  docker rm -f "${MONGO_CONTAINER_NAME}" > /dev/null 2>&1 || \
    echo "There is no docker container named ${MONGO_CONTAINER_NAME}"

  docker run -d \
    --name "${MONGO_CONTAINER_NAME}" \
    "${MONGO_IMAGE_NAME}"

  docker run -d \
    --name "${CONTAINER_NAME}" \
    --link ${MONGO_CONTAINER_NAME}:mongo \
    -e SPRING_DATA_MONGODB_HOST=mongo \
    "${IMAGE_NAME}"
}

acceptance-test() {
  docker run -i \
    --rm \
    --name "${ACCEPTANCE_TEST_CONTAINER_NAME}" \
    --link "${CONTAINER_NAME}":backend \
    -e ACCEPTANCE_TEST_BACKEND_HOST=backend \
    "${ACCEPTANCE_TEST_IMAGE_NAME}"
}

parse-args() {
  METHOD=${1}
}

main() {
  parse-args "${@}"
  ${METHOD}
}

main "${@}"
