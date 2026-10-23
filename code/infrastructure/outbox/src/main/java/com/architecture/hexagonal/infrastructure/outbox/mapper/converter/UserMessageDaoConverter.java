package com.architecture.hexagonal.infrastructure.outbox.mapper.converter;

import com.architecture.hexagonal.contract.outbound.event.rabbitmq.model.UserData;
import lombok.experimental.UtilityClass;
import org.mapstruct.Named;
import tools.jackson.databind.ObjectMapper;

@UtilityClass
public class UserMessageDaoConverter {

  public static final String TO_USER_DATA = "toUserData";

  private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

  @Named(TO_USER_DATA)
  public static UserData toUserData(final Object object) {
    return OBJECT_MAPPER.convertValue(object, UserData.class);
  }
}
