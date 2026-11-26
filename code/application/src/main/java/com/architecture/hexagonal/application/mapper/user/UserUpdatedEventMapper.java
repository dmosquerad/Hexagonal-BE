package com.architecture.hexagonal.application.mapper.user;

import com.architecture.hexagonal.application.mapper.converter.EmailVoConverter;
import com.architecture.hexagonal.domain.model.entity.user.User;
import com.architecture.hexagonal.domain.model.event.user.UserUpdatedEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = EmailVoConverter.class)
public interface UserUpdatedEventMapper {
  UserUpdatedEventMapper INSTANCE = Mappers.getMapper(UserUpdatedEventMapper.class);

  @Mapping(source = "email", target = "email", qualifiedByName = EmailVoConverter.TO_EMAIL)
  UserUpdatedEvent toUserUpdatedEvent(User user);
}
