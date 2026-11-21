package com.architecture.hexagonal.infrastructure.outbound.event.rabbitmq.mapper.user;

import com.architecture.hexagonal.contract.bus.model.user.event.UserCreatedEvent;
import com.architecture.hexagonal.contract.bus.model.user.event.UserDeletedEvent;
import com.architecture.hexagonal.contract.bus.model.user.event.UserUpdatedEvent;
import com.architecture.hexagonal.contract.outbound.event.rabbitmq.model.UserCreated;
import com.architecture.hexagonal.contract.outbound.event.rabbitmq.model.UserDeleted;
import com.architecture.hexagonal.contract.outbound.event.rabbitmq.model.UserUpdated;
import com.architecture.hexagonal.domain.model.vo.payload.MessageHeaderVo;
import com.architecture.hexagonal.infrastructure.outbound.event.rabbitmq.config.MapstructConfig;
import com.architecture.hexagonal.infrastructure.outbound.event.rabbitmq.mapper.converter.DateVoConverter;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapstructConfig.class, uses = DateVoConverter.class)
public interface UserEventMessageMapper {

  @Mapping(source = "event", target = "userData")
  @Mapping(source = "messageHeader.messageId", target = "messageHeader.messageId")
  @Mapping(
      source = "messageHeader.messageDate",
      target = "messageHeader.messageDate",
      qualifiedByName = DateVoConverter.TO_OFFSET_DATE_TIME)
  @Mapping(source = "messageHeader", target = "messageHeader")
  UserCreated toUserCreated(UserCreatedEvent event, MessageHeaderVo messageHeader);

  @Mapping(source = "event", target = "userData")
  @Mapping(source = "messageHeader.messageId", target = "messageHeader.messageId")
  @Mapping(
      source = "messageHeader.messageDate",
      target = "messageHeader.messageDate",
      qualifiedByName = DateVoConverter.TO_OFFSET_DATE_TIME)
  @Mapping(source = "messageHeader", target = "messageHeader")
  UserUpdated toUserUpdated(UserUpdatedEvent event, MessageHeaderVo messageHeader);

  @Mapping(source = "event", target = "userData")
  @Mapping(source = "messageHeader.messageId", target = "messageHeader.messageId")
  @Mapping(
      source = "messageHeader.messageDate",
      target = "messageHeader.messageDate",
      qualifiedByName = DateVoConverter.TO_OFFSET_DATE_TIME)
  @Mapping(source = "messageHeader", target = "messageHeader")
  UserDeleted toUserDeleted(UserDeletedEvent event, MessageHeaderVo messageHeader);
}
