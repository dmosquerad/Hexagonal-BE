package com.architecture.hexagonal.infrastructure.outbound.message.mapper;

import com.architecture.hexagonal.domain.model.entity.User;
import com.architecture.hexagonal.infrastructure.outbound.message.data.UserCreated;
import com.architecture.hexagonal.infrastructure.outbound.message.data.UserDeleted;
import com.architecture.hexagonal.infrastructure.outbound.message.data.UserUpdated;
import com.architecture.hexagonal.infrastructure.outbound.message.config.MapstructConfig;
import com.architecture.hexagonal.infrastructure.outbound.message.mapper.converter.EmailVoToStringConverter;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapstructConfig.class, uses = EmailVoToStringConverter.class)
public interface UserMessageDaoMapper {

  @Mapping(source = "email", target = "email", qualifiedByName = EmailVoToStringConverter.TO_EMAIL)
  UserCreated toUserCreated(User user);

  @Mapping(source = "email", target = "email", qualifiedByName = EmailVoToStringConverter.TO_EMAIL)
  UserUpdated toUserUpdated(User user);

  @Mapping(source = "email", target = "email", qualifiedByName = EmailVoToStringConverter.TO_EMAIL)
  UserDeleted toUserDeleted(User user);
}
