#!/bin/bash

/opt/kafka/bin/kafka-topics.sh \
  --bootstrap-server "kafka:29092" \
  --create \
  --if-not-exists \
  --topic user \
  --partitions 3 \
  --replication-factor 1