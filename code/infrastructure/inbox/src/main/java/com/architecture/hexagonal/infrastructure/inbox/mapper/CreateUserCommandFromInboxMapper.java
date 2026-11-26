package com.architecture.hexagonal.infrastructure.inbox.mapper;

import com.architecture.hexagonal.application.model.command.user.CreateUserCommand;
import com.architecture.hexagonal.contract.inbound.messaging.kafka.model.UserCreatedData;
import com.architecture.hexagonal.infrastructure.inbox.config.mapstruct.MapstructConfig;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapstructConfig.class)
public interface CreateUserCommandFromInboxMapper {

  @Mapping(target = ".", source = "userCreatedData")
  CreateUserCommand toCreateUserCommand(UserCreatedData userCreatedData);
}
