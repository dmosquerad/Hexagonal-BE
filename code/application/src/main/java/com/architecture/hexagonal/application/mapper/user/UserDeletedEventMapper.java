package com.architecture.hexagonal.application.mapper.user;

import com.architecture.hexagonal.application.mapper.converter.EmailVoConverter;
import com.architecture.hexagonal.domain.model.entity.user.User;
import com.architecture.hexagonal.domain.model.event.user.UserDeletedEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = EmailVoConverter.class)
public interface UserDeletedEventMapper {
  UserDeletedEventMapper INSTANCE = Mappers.getMapper(UserDeletedEventMapper.class);

  @Mapping(source = "email", target = "email", qualifiedByName = EmailVoConverter.TO_EMAIL)
  UserDeletedEvent toUserDeletedEvent(User user);
}
