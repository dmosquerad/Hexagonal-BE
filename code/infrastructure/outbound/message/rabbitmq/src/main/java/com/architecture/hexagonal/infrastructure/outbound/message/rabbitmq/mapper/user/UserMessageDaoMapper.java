package com.architecture.hexagonal.infrastructure.outbound.message.rabbitmq.mapper.user;

import com.architecture.hexagonal.contract.outbound.rabbitmq.model.UserCreated;
import com.architecture.hexagonal.contract.outbound.rabbitmq.model.UserDeleted;
import com.architecture.hexagonal.contract.outbound.rabbitmq.model.UserUpdated;
import com.architecture.hexagonal.domain.model.entity.user.User;
import com.architecture.hexagonal.domain.model.vo.payload.MessageHeaderVo;
import com.architecture.hexagonal.infrastructure.outbound.message.rabbitmq.config.MapstructConfig;
import com.architecture.hexagonal.infrastructure.outbound.message.rabbitmq.mapper.converter.EmailConverter;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapstructConfig.class, uses = EmailConverter.class)
public interface UserMessageDaoMapper {

  @Mapping(source = "user.userId", target = "data.userId")
  @Mapping(source = "user.name", target = "data.name")
  @Mapping(source = "user.email", target = "data.email", qualifiedByName = EmailConverter.TO_EMAIL)
  @Mapping(source = "messageHeader", target = "messageHeader")
  UserCreated toUserCreated(User user, MessageHeaderVo messageHeader);

  @Mapping(source = "user.userId", target = "data.userId")
  @Mapping(source = "user.name", target = "data.name")
  @Mapping(source = "user.email", target = "data.email", qualifiedByName = EmailConverter.TO_EMAIL)
  @Mapping(source = "messageHeader", target = "messageHeader")
  UserUpdated toUserUpdated(User user, MessageHeaderVo messageHeader);

  @Mapping(source = "user.userId", target = "data.userId")
  @Mapping(source = "user.name", target = "data.name")
  @Mapping(source = "user.email", target = "data.email", qualifiedByName = EmailConverter.TO_EMAIL)
  @Mapping(source = "messageHeader", target = "messageHeader")
  UserDeleted toUserDeleted(User user, MessageHeaderVo messageHeader);
}
