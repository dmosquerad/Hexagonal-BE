package com.architecture.hexagonal.infrastructure.inbound.messaging.kafka.mapper;

import com.architecture.hexagonal.application.model.command.user.DeleteUserCommand;
import com.architecture.hexagonal.contract.inbound.messaging.kafka.model.UserDeletedPayload;
import com.architecture.hexagonal.infrastructure.inbound.messaging.kafka.config.MapstructConfig;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapstructConfig.class)
public interface DeleteUserCommandMapper {

  @Mapping(target = ".", source = "data_")
  DeleteUserCommand toDeleteUserCommand(UserDeletedPayload userDeleted);
}
