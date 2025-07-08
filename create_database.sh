#!/usr/bin/env bash
set -euo pipefail

NODES=(crdb-node1 crdb-node2 crdb-node3)

SQL='
CREATE DATABASE IF NOT EXISTS "sensorDataCollector";
CREATE DATABASE IF NOT EXISTS "notificationDispatcher";
CREATE DATABASE IF NOT EXISTS "enviromentAnalyzer";
'

for node in "${NODES[@]}"; do
  docker exec -i "$node" ./cockroach sql --insecure -e "$SQL"
done