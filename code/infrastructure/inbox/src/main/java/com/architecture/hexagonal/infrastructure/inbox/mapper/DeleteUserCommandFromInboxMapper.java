package com.architecture.hexagonal.infrastructure.inbox.mapper;

import com.architecture.hexagonal.application.model.command.user.DeleteUserCommand;
import com.architecture.hexagonal.contract.inbound.messaging.kafka.model.UserDeletedData;
import com.architecture.hexagonal.infrastructure.inbox.config.mapstruct.MapstructConfig;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapstructConfig.class)
public interface DeleteUserCommandFromInboxMapper {

  @Mapping(target = ".", source = "userDeletedData")
  DeleteUserCommand toDeleteUserCommand(UserDeletedData userDeletedData);
}
