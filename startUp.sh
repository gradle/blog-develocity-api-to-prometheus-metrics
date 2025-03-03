#!/bin/bash

docker run --name dvme --detach --restart always --memory=2G -p 8081:8081 -e DVSERVER=ge.gradle.org -e DVKEY=ge-gradle-org.key frankzhu2003/dv-prom-metrics:latest

docker run --name dvmepse --detach --restart always --memory=2G -p 8082:8081 -e DVSERVER=develocity-field.gradle.com -e DVKEY=develocity-field.key frankzhu2003/dv-prom-metrics:latest

docker run --restart always --detach     --name my-prometheus     --publish 9090:9090     --volume prometheus-volume:/prometheus     --volume "$(pwd)"/prometheus.yml:/etc/prometheus/prometheus.yml  --add-host host.docker.internal=host-gateway   prom/prometheus
