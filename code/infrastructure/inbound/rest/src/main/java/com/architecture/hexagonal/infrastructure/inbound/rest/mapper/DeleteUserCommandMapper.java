package com.architecture.hexagonal.infrastructure.inbound.rest.mapper;

import com.architecture.hexagonal.application.input.command.DeleteUserCommand;
import com.architecture.hexagonal.infrastructure.inbound.rest.config.MapstructConfig;
import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapstructConfig.class)
public interface DeleteUserCommandMapper {

  @Mapping(source = "userUuid", target = "userId")
  DeleteUserCommand toDeleteUserCommand(UUID userUuid);
}
