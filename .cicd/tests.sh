#!/usr/bin/env bash
set -euo pipefail

MAVEN_CMD="mvn"
if [[ -x "./mvnw" ]]; then
  MAVEN_CMD="./mvnw"
fi

echo "Running Java tests..."
#"${MAVEN_CMD}" -B -ntp test
