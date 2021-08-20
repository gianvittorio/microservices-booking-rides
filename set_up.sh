#!/bin/bash

mvn clean install
if [[ $? -eq 0 ]]; then
    echo OK
else
    echo FAIL
    exit 1
fi

docker-compose up --build -d
