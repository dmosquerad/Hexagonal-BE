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

  @Mapping(target = "messageHeader", ignore = true)
  @Mapping(source = "userId", target = "data.userId")
  @Mapping(source = "name", target = "data.name")
  @Mapping(
      source = "email",
      target = "data.email",
      qualifiedByName = EmailVoToStringConverter.TO_EMAIL)
  UserCreated toUserCreated(User user);

  @Mapping(target = "messageHeader", ignore = true)
  @Mapping(source = "userId", target = "data.userId")
  @Mapping(source = "name", target = "data.name")
  @Mapping(
      source = "email",
      target = "data.email",
      qualifiedByName = EmailVoToStringConverter.TO_EMAIL)
  UserUpdated toUserUpdated(User user);

  @Mapping(target = "messageHeader", ignore = true)
  @Mapping(source = "userId", target = "data.userId")
  @Mapping(source = "name", target = "data.name")
  @Mapping(
      source = "email",
      target = "data.email",
      qualifiedByName = EmailVoToStringConverter.TO_EMAIL)
  UserDeleted toUserDeleted(User user);
}
