package com.architecture.hexagonal.infrastructure.inbound.messaging.kafka.mapper;

import com.architecture.hexagonal.application.model.command.inbox.RegisterInboxCommand;
import com.architecture.hexagonal.contract.inbound.messaging.kafka.model.UserCreatedPayload;
import com.architecture.hexagonal.contract.inbound.messaging.kafka.model.UserDeletedPayload;
import com.architecture.hexagonal.contract.inbound.messaging.kafka.model.UserUpdatedPayload;
import com.architecture.hexagonal.domain.model.vo.payload.ActionType;
import com.architecture.hexagonal.domain.model.vo.payload.AggregateTypeVo;
import com.architecture.hexagonal.infrastructure.inbound.messaging.kafka.config.MapstructConfig;
import com.architecture.hexagonal.infrastructure.inbound.messaging.kafka.mapper.converter.DateConverter;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapstructConfig.class, uses = DateConverter.class)
public interface RegisterInboxCommandMapper {

  @Mapping(target = "data", source = "message.data_")
  @Mapping(target = "aggregateType", constant = AggregateTypeVo.Naming.AGGREGATE_TYPE_USER)
  @Mapping(target = "aggregateId", ignore = true)
  @Mapping(target = "action", constant = ActionType.UserActionType.Naming.USER_CREATED_ACTION)
  @Mapping(target = "createdAt", ignore = true)
  @Mapping(
      target = "messageHeader.messageDate",
      source = "message.messageHeader.messageDate",
      qualifiedByName = DateConverter.TO_DATE)
  RegisterInboxCommand toRegisterInboxCommand(UserCreatedPayload message);

  @Mapping(target = "data", source = "message.data_")
  @Mapping(target = "aggregateType", constant = AggregateTypeVo.Naming.AGGREGATE_TYPE_USER)
  @Mapping(target = "aggregateId", source = "message.data_.userId")
  @Mapping(target = "action", constant = ActionType.UserActionType.Naming.USER_UPDATED_ACTION)
  @Mapping(target = "createdAt", ignore = true)
  @Mapping(
      target = "messageHeader.messageDate",
      source = "message.messageHeader.messageDate",
      qualifiedByName = DateConverter.TO_DATE)
  RegisterInboxCommand toRegisterInboxCommand(UserUpdatedPayload message);

  @Mapping(target = "data", source = "message.data_")
  @Mapping(target = "aggregateType", constant = AggregateTypeVo.Naming.AGGREGATE_TYPE_USER)
  @Mapping(target = "aggregateId", source = "message.data_.userId")
  @Mapping(target = "action", constant = ActionType.UserActionType.Naming.USER_DELETED_ACTION)
  @Mapping(target = "createdAt", ignore = true)
  @Mapping(
      target = "messageHeader.messageDate",
      source = "message.messageHeader.messageDate",
      qualifiedByName = DateConverter.TO_DATE)
  RegisterInboxCommand toRegisterInboxCommand(UserDeletedPayload message);
}
