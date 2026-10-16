package com.architecture.hexagonal.infrastructure.inbound.handler.kafka.mapper;

import com.architecture.hexagonal.contract.application.usecase.generated.inbox.RegisterInboxCommand;
import com.architecture.hexagonal.contract.application.usecase.generated.user.CreateUserCommand;
import com.architecture.hexagonal.contract.application.usecase.generated.user.DeleteUserCommand;
import com.architecture.hexagonal.contract.application.usecase.generated.user.UpdateUserCommand;
import com.architecture.hexagonal.contract.inbound.kafka.model.UserCreated;
import com.architecture.hexagonal.contract.inbound.kafka.model.UserDeleted;
import com.architecture.hexagonal.contract.inbound.kafka.model.UserUpdated;
import com.architecture.hexagonal.infrastructure.inbound.handler.kafka.config.MapstructConfig;
import com.architecture.hexagonal.infrastructure.inbound.handler.kafka.mapper.converter.UserKafkaMessageConverter;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapstructConfig.class, uses = UserKafkaMessageConverter.class)
public interface UserKafkaMessageMapper {

  @Mapping(target = ".", source = "data")
  CreateUserCommand toCreateUserCommand(UserCreated userCreated);

  @Mapping(target = ".", source = "data")
  UpdateUserCommand toUpdateUserCommand(UserUpdated userUpdated);

  @Mapping(target = ".", source = "data")
  DeleteUserCommand toDeleteUserCommand(UserDeleted userDeleted);

  @Mapping(
      target = "data",
      source = "message",
      qualifiedByName = UserKafkaMessageConverter.TO_OBJECT)
  @Mapping(target = "aggregateType", constant = "USER")
  @Mapping(target = "aggregateId", ignore = true)
  @Mapping(target = "action", constant = "USER_CREATED")
  @Mapping(target = "createdAt", ignore = true)
  RegisterInboxCommand toRegisterInboxCommand(UserCreated message);

  @Mapping(
      target = "data",
      source = "message",
      qualifiedByName = UserKafkaMessageConverter.TO_OBJECT)
  @Mapping(target = "aggregateType", constant = "USER")
  @Mapping(
      target = "aggregateId",
      source = "message.data.userId",
      qualifiedByName = UserKafkaMessageConverter.TO_OBJECT)
  @Mapping(target = "action", constant = "USER_UPDATED")
  @Mapping(target = "createdAt", ignore = true)
  RegisterInboxCommand toRegisterInboxCommand(UserUpdated message);

  @Mapping(
      target = "data",
      source = "message",
      qualifiedByName = UserKafkaMessageConverter.TO_OBJECT)
  @Mapping(target = "aggregateType", constant = "USER")
  @Mapping(
      target = "aggregateId",
      source = "message.data.userId",
      qualifiedByName = UserKafkaMessageConverter.TO_OBJECT)
  @Mapping(target = "action", constant = "USER_DELETED")
  @Mapping(target = "createdAt", ignore = true)
  RegisterInboxCommand toRegisterInboxCommand(UserDeleted message);
}
