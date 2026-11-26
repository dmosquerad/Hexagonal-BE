package com.architecture.hexagonal.infrastructure.inbound.messaging.kafka.mapper;

import com.architecture.hexagonal.application.model.command.user.CreateUserCommand;
import com.architecture.hexagonal.contract.inbound.messaging.kafka.model.UserCreatedPayload;
import com.architecture.hexagonal.infrastructure.inbound.messaging.kafka.config.MapstructConfig;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapstructConfig.class)
public interface CreateUserCommandMapper {

  @Mapping(target = ".", source = "data_")
  CreateUserCommand toCreateUserCommand(UserCreatedPayload userCreated);
}
