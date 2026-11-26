package com.architecture.hexagonal.infrastructure.outbound.event.rabbitmq.mapper.user;

import com.architecture.hexagonal.contract.outbound.event.rabbitmq.model.UserDeleted;
import com.architecture.hexagonal.domain.model.event.user.UserDeletedEvent;
import com.architecture.hexagonal.domain.model.vo.payload.MessageHeaderVo;
import com.architecture.hexagonal.infrastructure.outbound.event.rabbitmq.config.MapstructConfig;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapstructConfig.class)
public interface UserDeletedEventMessageMapper {

  @Mapping(source = "event", target = "userData")
  @Mapping(target = "messageHeader.messageId", source = "messageHeader.messageId")
  @Mapping(
      target = "messageHeader.messageDate",
      source = "messageHeader.messageDate.offsetDateTime")
  UserDeleted toUserDeleted(UserDeletedEvent event, MessageHeaderVo messageHeader);
}
