package com.architecture.hexagonal.infrastructure.outbox.mapper.user;

import com.architecture.hexagonal.contract.outbound.event.rabbitmq.model.UserCreated;
import com.architecture.hexagonal.contract.outbound.event.rabbitmq.model.UserDeleted;
import com.architecture.hexagonal.contract.outbound.event.rabbitmq.model.UserUpdated;
import com.architecture.hexagonal.domain.model.vo.payload.PayloadVo;
import com.architecture.hexagonal.infrastructure.outbox.config.mapstruct.MapstructConfig;
import com.architecture.hexagonal.infrastructure.outbox.mapper.converter.UserMessageDaoConverter;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapstructConfig.class, uses = UserMessageDaoConverter.class)
public interface UserFromOutboxMapper {

  @Mapping(
      source = "payload.data",
      target = "userData",
      qualifiedByName = UserMessageDaoConverter.TO_USER_DATA)
  @Mapping(source = "payload.messageHeader", target = "messageHeader")
  UserCreated toUserCreated(PayloadVo payload);

  @Mapping(
      source = "payload.data",
      target = "userData",
      qualifiedByName = UserMessageDaoConverter.TO_USER_DATA)
  @Mapping(source = "payload.messageHeader", target = "messageHeader")
  UserUpdated toUserUpdated(PayloadVo payload);

  @Mapping(
      source = "payload.data",
      target = "userData",
      qualifiedByName = UserMessageDaoConverter.TO_USER_DATA)
  @Mapping(source = "payload.messageHeader", target = "messageHeader")
  UserDeleted toUserDeleted(PayloadVo payload);
}
