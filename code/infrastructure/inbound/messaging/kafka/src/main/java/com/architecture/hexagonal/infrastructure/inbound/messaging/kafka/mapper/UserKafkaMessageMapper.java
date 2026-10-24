package com.architecture.hexagonal.infrastructure.inbound.messaging.kafka.mapper;

import com.architecture.hexagonal.contract.bus.model.inbox.command.RegisterInboxCommand;
import com.architecture.hexagonal.contract.bus.model.user.command.CreateUserCommand;
import com.architecture.hexagonal.contract.bus.model.user.command.DeleteUserCommand;
import com.architecture.hexagonal.contract.bus.model.user.command.UpdateUserCommand;
import com.architecture.hexagonal.contract.inbound.messaging.kafka.model.UserCreatedPayload;
import com.architecture.hexagonal.contract.inbound.messaging.kafka.model.UserDeletedPayload;
import com.architecture.hexagonal.contract.inbound.messaging.kafka.model.UserUpdatedPayload;
import com.architecture.hexagonal.infrastructure.inbound.messaging.kafka.config.MapstructConfig;
import com.architecture.hexagonal.infrastructure.inbound.messaging.kafka.mapper.converter.UserKafkaMessageConverter;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapstructConfig.class, uses = UserKafkaMessageConverter.class)
public interface UserKafkaMessageMapper {

  @Mapping(target = ".", source = "data_")
  CreateUserCommand toCreateUserCommand(UserCreatedPayload userCreated);

  @Mapping(target = ".", source = "data_")
  UpdateUserCommand toUpdateUserCommand(UserUpdatedPayload userUpdated);

  @Mapping(target = ".", source = "data_")
  DeleteUserCommand toDeleteUserCommand(UserDeletedPayload userDeleted);

  @Mapping(
      target = "data",
      source = "message.data_",
      qualifiedByName = UserKafkaMessageConverter.TO_OBJECT)
  @Mapping(target = "aggregateType", constant = "USER")
  @Mapping(target = "aggregateId", ignore = true)
  @Mapping(target = "action", constant = "USER_CREATED")
  @Mapping(target = "createdAt", ignore = true)
  RegisterInboxCommand toRegisterInboxCommand(UserCreatedPayload message);

  @Mapping(
      target = "data",
      source = "message.data_",
      qualifiedByName = UserKafkaMessageConverter.TO_OBJECT)
  @Mapping(target = "aggregateType", constant = "USER")
  @Mapping(
      target = "aggregateId",
      source = "message.data_.userId",
      qualifiedByName = UserKafkaMessageConverter.TO_OBJECT)
  @Mapping(target = "action", constant = "USER_UPDATED")
  @Mapping(target = "createdAt", ignore = true)
  RegisterInboxCommand toRegisterInboxCommand(UserUpdatedPayload message);

  @Mapping(
      target = "data",
      source = "message.data_",
      qualifiedByName = UserKafkaMessageConverter.TO_OBJECT)
  @Mapping(target = "aggregateType", constant = "USER")
  @Mapping(
      target = "aggregateId",
      source = "message.data_.userId",
      qualifiedByName = UserKafkaMessageConverter.TO_OBJECT)
  @Mapping(target = "action", constant = "USER_DELETED")
  @Mapping(target = "createdAt", ignore = true)
  RegisterInboxCommand toRegisterInboxCommand(UserDeletedPayload message);
}
