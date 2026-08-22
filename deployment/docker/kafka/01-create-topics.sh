#!/bin/bash

/opt/kafka/bin/kafka-topics.sh \
  --bootstrap-server "kafka:29092" \
  --create \
  --if-not-exists \
  --topic user.created \
  --partitions 3 \
  --replication-factor 1
  
/opt/kafka/bin/kafka-topics.sh \
  --bootstrap-server "kafka:29092" \
  --create \
  --if-not-exists \
  --topic user.updated \
  --partitions 3 \
  --replication-factor 1

/opt/kafka/bin/kafka-topics.sh \
  --bootstrap-server "kafka:29092" \
  --create \
  --if-not-exists \
  --topic user.deleted \
  --partitions 3 \
  --replication-factor 1