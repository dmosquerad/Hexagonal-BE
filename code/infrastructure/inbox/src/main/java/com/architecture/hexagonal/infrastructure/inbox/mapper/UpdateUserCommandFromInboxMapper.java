package com.architecture.hexagonal.infrastructure.inbox.mapper;

import com.architecture.hexagonal.application.model.command.user.UpdateUserCommand;
import com.architecture.hexagonal.contract.inbound.messaging.kafka.model.UserUpdatedData;
import com.architecture.hexagonal.infrastructure.inbox.config.mapstruct.MapstructConfig;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapstructConfig.class)
public interface UpdateUserCommandFromInboxMapper {

  @Mapping(target = ".", source = "userUpdatedData")
  UpdateUserCommand toUpdateUserCommand(UserUpdatedData userUpdatedData);
}
