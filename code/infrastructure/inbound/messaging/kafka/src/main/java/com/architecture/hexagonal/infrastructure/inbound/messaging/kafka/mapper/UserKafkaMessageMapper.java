package com.architecture.hexagonal.infrastructure.inbound.messaging.kafka.mapper;

import com.architecture.hexagonal.contract.bus.model.inbox.command.RegisterInboxCommand;
import com.architecture.hexagonal.contract.bus.model.user.command.CreateUserCommand;
import com.architecture.hexagonal.contract.bus.model.user.command.DeleteUserCommand;
import com.architecture.hexagonal.contract.bus.model.user.command.UpdateUserCommand;
import com.architecture.hexagonal.contract.inbound.messaging.kafka.model.UserCreatedPayload;
import com.architecture.hexagonal.contract.inbound.messaging.kafka.model.UserDeletedPayload;
import com.architecture.hexagonal.contract.inbound.messaging.kafka.model.UserUpdatedPayload;
import com.architecture.hexagonal.domain.model.vo.payload.ActionType;
import com.architecture.hexagonal.domain.model.vo.payload.AggregateTypeVo;
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
  @Mapping(target = "aggregateType", constant = AggregateTypeVo.Naming.AGGREGATE_TYPE_USER)
  @Mapping(target = "aggregateId", ignore = true)
  @Mapping(target = "action", constant = ActionType.UserActionType.Naming.USER_CREATED_ACTION)
  @Mapping(target = "createdAt", ignore = true)
  RegisterInboxCommand toRegisterInboxCommand(UserCreatedPayload message);

  @Mapping(
      target = "data",
      source = "message.data_",
      qualifiedByName = UserKafkaMessageConverter.TO_OBJECT)
  @Mapping(target = "aggregateType", constant = AggregateTypeVo.Naming.AGGREGATE_TYPE_USER)
  @Mapping(
      target = "aggregateId",
      source = "message.data_.userId",
      qualifiedByName = UserKafkaMessageConverter.TO_OBJECT)
  @Mapping(target = "action", constant = ActionType.UserActionType.Naming.USER_UPDATED_ACTION)
  @Mapping(target = "createdAt", ignore = true)
  RegisterInboxCommand toRegisterInboxCommand(UserUpdatedPayload message);

  @Mapping(
      target = "data",
      source = "message.data_",
      qualifiedByName = UserKafkaMessageConverter.TO_OBJECT)
  @Mapping(target = "aggregateType", constant = AggregateTypeVo.Naming.AGGREGATE_TYPE_USER)
  @Mapping(
      target = "aggregateId",
      source = "message.data_.userId",
      qualifiedByName = UserKafkaMessageConverter.TO_OBJECT)
  @Mapping(target = "action", constant = ActionType.UserActionType.Naming.USER_DELETED_ACTION)
  @Mapping(target = "createdAt", ignore = true)
  RegisterInboxCommand toRegisterInboxCommand(UserDeletedPayload message);
}
