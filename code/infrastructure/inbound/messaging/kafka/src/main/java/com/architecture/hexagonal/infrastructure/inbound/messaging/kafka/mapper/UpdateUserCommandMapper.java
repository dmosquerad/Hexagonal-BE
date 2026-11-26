package com.architecture.hexagonal.infrastructure.inbound.messaging.kafka.mapper;

import com.architecture.hexagonal.application.model.command.user.UpdateUserCommand;
import com.architecture.hexagonal.contract.inbound.messaging.kafka.model.UserUpdatedPayload;
import com.architecture.hexagonal.infrastructure.inbound.messaging.kafka.config.MapstructConfig;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapstructConfig.class)
public interface UpdateUserCommandMapper {

  @Mapping(target = ".", source = "data_")
  UpdateUserCommand toUpdateUserCommand(UserUpdatedPayload userUpdated);
}
