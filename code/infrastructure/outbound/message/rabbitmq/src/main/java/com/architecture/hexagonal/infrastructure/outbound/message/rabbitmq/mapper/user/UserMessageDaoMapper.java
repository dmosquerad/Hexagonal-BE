package com.architecture.hexagonal.infrastructure.outbound.message.rabbitmq.mapper.user;

import com.architecture.hexagonal.domain.model.entity.user.User;
import com.architecture.hexagonal.domain.model.vo.outbox.MessageHeaderVo;
import com.architecture.hexagonal.infrastructure.outbound.message.rabbitmq.config.MapstructConfig;
import com.architecture.hexagonal.infrastructure.outbound.message.rabbitmq.data.UserCreated;
import com.architecture.hexagonal.infrastructure.outbound.message.rabbitmq.data.UserDeleted;
import com.architecture.hexagonal.infrastructure.outbound.message.rabbitmq.data.UserUpdated;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapstructConfig.class)
public interface UserMessageDaoMapper {

  @Mapping(source = "user.userId", target = "data.userId")
  @Mapping(source = "user.name", target = "data.name")
  @Mapping(source = "user.email", target = "data.email")
  UserCreated toUserCreated(User user, MessageHeaderVo messageHeader);

  @Mapping(source = "user.userId", target = "data.userId")
  @Mapping(source = "user.name", target = "data.name")
  @Mapping(source = "user.email", target = "data.email")
  UserUpdated toUserUpdated(User user, MessageHeaderVo messageHeader);

  @Mapping(source = "user.userId", target = "data.userId")
  @Mapping(source = "user.name", target = "data.name")
  @Mapping(source = "user.email", target = "data.email")
  UserDeleted toUserDeleted(User user, MessageHeaderVo messageHeader);
}
