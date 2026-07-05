package com.architecture.hexagonal.infrastructure.outbound.message.rabbitmq.mapper.user;

import com.architecture.hexagonal.domain.model.aggregate.user.User;
import com.architecture.hexagonal.infrastructure.outbound.message.rabbitmq.config.MapstructConfig;
import com.architecture.hexagonal.infrastructure.outbound.message.rabbitmq.data.UserCreated;
import com.architecture.hexagonal.infrastructure.outbound.message.rabbitmq.data.UserDeleted;
import com.architecture.hexagonal.infrastructure.outbound.message.rabbitmq.data.UserUpdated;
import com.architecture.hexagonal.infrastructure.outbound.message.rabbitmq.mapper.converter.EmailVoToStringConverter;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapstructConfig.class, uses = EmailVoToStringConverter.class)
public interface UserMessageDaoMapper {

  @Mapping(source = "user.userId", target = "userId")
  @Mapping(source = "user.name", target = "name")
  @Mapping(source = "email", target = "email", qualifiedByName = EmailVoToStringConverter.TO_EMAIL)
  UserCreated toUserCreated(User user);

  @Mapping(source = "user.userId", target = "userId")
  @Mapping(source = "user.name", target = "name")
  @Mapping(source = "email", target = "email", qualifiedByName = EmailVoToStringConverter.TO_EMAIL)
  UserUpdated toUserUpdated(User user);

  @Mapping(source = "user.userId", target = "userId")
  @Mapping(source = "user.name", target = "name")
  @Mapping(source = "email", target = "email", qualifiedByName = EmailVoToStringConverter.TO_EMAIL)
  UserDeleted toUserDeleted(User user);
}
