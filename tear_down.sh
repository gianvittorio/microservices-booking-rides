#!/bin/bash

docker-compose down && docker volume rm -f $(docker volume ls --filter name=microservices-booking-rides)
