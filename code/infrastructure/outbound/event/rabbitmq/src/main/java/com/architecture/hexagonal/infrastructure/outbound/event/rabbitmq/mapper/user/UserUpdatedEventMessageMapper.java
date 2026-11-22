package com.architecture.hexagonal.infrastructure.outbound.event.rabbitmq.mapper.user;

import com.architecture.hexagonal.contract.bus.model.user.event.UserUpdatedEvent;
import com.architecture.hexagonal.contract.outbound.event.rabbitmq.model.UserUpdated;
import com.architecture.hexagonal.domain.model.vo.payload.MessageHeaderVo;
import com.architecture.hexagonal.infrastructure.outbound.event.rabbitmq.config.MapstructConfig;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapstructConfig.class)
public interface UserUpdatedEventMessageMapper {

  @Mapping(source = "event", target = "userData")
  @Mapping(target = "messageHeader.messageId", source = "messageHeader.messageId")
  @Mapping(
      target = "messageHeader.messageDate",
      source = "messageHeader.messageDate.offsetDateTime")
  UserUpdated toUserUpdated(UserUpdatedEvent event, MessageHeaderVo messageHeader);
}
