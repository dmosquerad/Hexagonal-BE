package com.architecture.hexagonal.infrastructure.outbound.outbox.mapper.user;

import com.architecture.hexagonal.contract.outbound.rabbitmq.UserCreated;
import com.architecture.hexagonal.contract.outbound.rabbitmq.UserDeleted;
import com.architecture.hexagonal.contract.outbound.rabbitmq.UserUpdated;
import com.architecture.hexagonal.domain.model.vo.outbox.PayloadVo;
import com.architecture.hexagonal.infrastructure.outbound.outbox.config.mapstruct.MapstructConfig;
import com.architecture.hexagonal.infrastructure.outbound.outbox.mapper.converter.UserMessageDaoConverter;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapstructConfig.class, uses = UserMessageDaoConverter.class)
public interface UserFromOutboxMapper {

  @Mapping(
      source = "payload.data",
      target = "data",
      qualifiedByName = UserMessageDaoConverter.TO_USER_DATA)
  @Mapping(source = "payload.messageHeader", target = "messageHeader")
  UserCreated toUserCreated(PayloadVo payload);

  @Mapping(
      source = "payload.data",
      target = "data",
      qualifiedByName = UserMessageDaoConverter.TO_USER_DATA)
  @Mapping(source = "payload.messageHeader", target = "messageHeader")
  UserUpdated toUserUpdated(PayloadVo payload);

  @Mapping(
      source = "payload.data",
      target = "data",
      qualifiedByName = UserMessageDaoConverter.TO_USER_DATA)
  @Mapping(source = "payload.messageHeader", target = "messageHeader")
  UserDeleted toUserDeleted(PayloadVo payload);
}
