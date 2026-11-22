package com.architecture.hexagonal.infrastructure.outbound.event.rabbitmq.mapper.user;

import com.architecture.hexagonal.contract.bus.model.user.event.UserCreatedEvent;
import com.architecture.hexagonal.contract.outbound.event.rabbitmq.model.UserCreated;
import com.architecture.hexagonal.domain.model.vo.payload.MessageHeaderVo;
import com.architecture.hexagonal.infrastructure.outbound.event.rabbitmq.config.MapstructConfig;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapstructConfig.class)
public interface UserCreatedEventMessageMapper {

  @Mapping(source = "event", target = "userData")
  @Mapping(target = "messageHeader.messageId", source = "messageHeader.messageId")
  @Mapping(
      target = "messageHeader.messageDate",
      source = "messageHeader.messageDate.offsetDateTime")
  UserCreated toUserCreated(UserCreatedEvent event, MessageHeaderVo messageHeader);
}
