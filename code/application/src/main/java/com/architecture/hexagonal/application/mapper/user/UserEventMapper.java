package com.architecture.hexagonal.application.mapper.user;

import com.architecture.hexagonal.application.mapper.converter.EmailVoConverter;
import com.architecture.hexagonal.contract.bus.model.user.event.UserCreatedEvent;
import com.architecture.hexagonal.contract.bus.model.user.event.UserDeletedEvent;
import com.architecture.hexagonal.contract.bus.model.user.event.UserUpdatedEvent;
import com.architecture.hexagonal.domain.model.entity.user.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = EmailVoConverter.class)
public interface UserEventMapper {
  UserEventMapper INSTANCE = Mappers.getMapper(UserEventMapper.class);

  @Mapping(source = "email", target = "email", qualifiedByName = EmailVoConverter.TO_EMAIL)
  UserCreatedEvent toUserCreatedEvent(User user);

  @Mapping(source = "email", target = "email", qualifiedByName = EmailVoConverter.TO_EMAIL)
  UserUpdatedEvent toUserUpdatedEvent(User user);

  @Mapping(source = "email", target = "email", qualifiedByName = EmailVoConverter.TO_EMAIL)
  UserDeletedEvent toUserDeletedEvent(User user);
}
