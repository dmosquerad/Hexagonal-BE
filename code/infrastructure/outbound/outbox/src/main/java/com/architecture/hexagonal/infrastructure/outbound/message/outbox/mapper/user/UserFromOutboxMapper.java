package com.architecture.hexagonal.infrastructure.outbound.message.outbox.mapper.user;

import com.architecture.hexagonal.domain.model.vo.outbox.PayloadVo;
import com.architecture.hexagonal.infrastructure.outbound.message.outbox.config.mapstruct.MapstructConfig;
import com.architecture.hexagonal.infrastructure.outbound.message.outbox.mapper.converter.UserMessageDaoConverter;
import com.architecture.hexagonal.infrastructure.outbound.message.rabbitmq.data.UserCreated;
import com.architecture.hexagonal.infrastructure.outbound.message.rabbitmq.data.UserDeleted;
import com.architecture.hexagonal.infrastructure.outbound.message.rabbitmq.data.UserUpdated;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapstructConfig.class, uses = UserMessageDaoConverter.class)
public interface UserFromOutboxMapper {

  @Mapping(
      source = "payload.data",
      target = "data",
      qualifiedByName = UserMessageDaoConverter.TO_USER)
  @Mapping(source = "payload.messageHeader", target = "messageHeader")
  UserCreated toUserCreated(PayloadVo payload);

  @Mapping(
      source = "payload.data",
      target = "data",
      qualifiedByName = UserMessageDaoConverter.TO_USER)
  @Mapping(source = "payload.messageHeader", target = "messageHeader")
  UserUpdated toUserUpdated(PayloadVo payload);

  @Mapping(
      source = "payload.data",
      target = "data",
      qualifiedByName = UserMessageDaoConverter.TO_USER)
  @Mapping(source = "payload.messageHeader", target = "messageHeader")
  UserDeleted toUserDeleted(PayloadVo payload);
}
