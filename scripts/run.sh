#!/usr/bin/env bash
set -e

sh ./scripts/start-postgres.sh
mvn spring-boot:run
