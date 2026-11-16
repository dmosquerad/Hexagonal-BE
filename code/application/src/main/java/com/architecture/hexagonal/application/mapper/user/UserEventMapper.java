package com.architecture.hexagonal.application.mapper.user;

import com.architecture.hexagonal.application.config.mapstruct.MapstructConfig;
import com.architecture.hexagonal.contract.bus.model.user.event.UserCreatedEvent;
import com.architecture.hexagonal.contract.bus.model.user.event.UserDeletedEvent;
import com.architecture.hexagonal.contract.bus.model.user.event.UserUpdatedEvent;
import com.architecture.hexagonal.domain.model.entity.user.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapstructConfig.class)
public interface UserEventMapper {

  @Mapping(target = "email", source = "email.email")
  UserCreatedEvent toUserCreatedEvent(User user);

  @Mapping(target = "email", source = "email.email")
  UserUpdatedEvent toUserUpdatedEvent(User user);

  @Mapping(target = "email", source = "email.email")
  UserDeletedEvent toUserDeletedEvent(User user);
}
