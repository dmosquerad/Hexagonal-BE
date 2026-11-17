package com.architecture.hexagonal.application.mapper.user;

import com.architecture.hexagonal.application.config.mapstruct.MapstructConfig;
import com.architecture.hexagonal.contract.bus.model.user.event.UserUpdatedEvent;
import com.architecture.hexagonal.domain.model.entity.user.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(config = MapstructConfig.class)
public interface UserUpdatedEventMapper {

  UserUpdatedEventMapper INSTANCE = Mappers.getMapper(UserUpdatedEventMapper.class);

  @Mapping(target = "email", source = "email.email")
  UserUpdatedEvent toUserUpdatedEvent(User user);
}
