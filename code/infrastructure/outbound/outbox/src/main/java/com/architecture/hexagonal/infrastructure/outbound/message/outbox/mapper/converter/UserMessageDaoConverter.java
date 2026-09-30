package com.architecture.hexagonal.infrastructure.outbound.message.outbox.mapper.converter;

import com.architecture.hexagonal.domain.model.entity.user.User;
import lombok.experimental.UtilityClass;
import org.mapstruct.Named;
import tools.jackson.databind.ObjectMapper;

@UtilityClass
public class UserMessageDaoConverter {

  public static final String TO_USER = "toUser";

  private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

  @Named(TO_USER)
  public static User toUser(final Object object) {
    return OBJECT_MAPPER.convertValue(object, User.class);
  }
}
