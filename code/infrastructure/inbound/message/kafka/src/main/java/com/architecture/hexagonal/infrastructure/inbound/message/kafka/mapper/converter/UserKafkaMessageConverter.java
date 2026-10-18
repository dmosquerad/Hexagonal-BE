package com.architecture.hexagonal.infrastructure.inbound.message.kafka.mapper.converter;

import lombok.experimental.UtilityClass;
import org.mapstruct.Named;

@UtilityClass
public class UserKafkaMessageConverter {

  public static final String TO_OBJECT = "toObject";

  @Named(TO_OBJECT)
  public static Object toObject(final Object message) {
    return message;
  }
}
