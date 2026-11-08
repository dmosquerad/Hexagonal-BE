package com.architecture.hexagonal.infrastructure.inbound.messaging.kafka.config;

import com.architecture.hexagonal.contract.inbound.messaging.kafka.model.UserCreatedPayload;
import com.architecture.hexagonal.contract.inbound.messaging.kafka.model.UserDeletedPayload;
import com.architecture.hexagonal.contract.inbound.messaging.kafka.model.UserUpdatedPayload;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import java.nio.charset.StandardCharsets;
import lombok.experimental.UtilityClass;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.Headers;

@UtilityClass
public class UserKafkaPayloadTypeResolver {

  public static JavaType resolveType(final Headers headers) {
    final Header eventTypeHeader = headers.lastHeader("X-Event-Type");

    final String eventType = new String(eventTypeHeader.value(), StandardCharsets.UTF_8);
    final Class<?> payloadType =
        switch (eventType) {
          case "create" -> UserCreatedPayload.class;
          case "update" -> UserUpdatedPayload.class;
          case "delete" -> UserDeletedPayload.class;
          default ->
              throw new SerializationException(
                  "Unsupported X-Event-Type Kafka header: " + eventType);
        };

    return TypeFactory.defaultInstance().constructType(payloadType);
  }
}
