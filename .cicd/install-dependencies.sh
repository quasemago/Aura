#!/usr/bin/env bash
set -euo pipefail

MAVEN_CMD="mvn"
if [[ -x "./mvnw" ]]; then
  MAVEN_CMD="./mvnw"
fi

echo "Resolving Maven dependencies..."
"${MAVEN_CMD}" -B -ntp -DskipTests dependency:go-offline
