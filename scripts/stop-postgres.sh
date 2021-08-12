#!/usr/bin/env bash
set -e

docker-compose -f src/main/docker/docker-compose.yml down --remove-orphans
