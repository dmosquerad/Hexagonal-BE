package com.architecture.hexagonal.infrastructure.inbound.rest.mapper;

import com.architecture.hexagonal.application.input.command.DeleteUserCommand;
import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface DeleteUserCommandMapper {

  @Mapping(source = "userUuid", target = "userId")
  DeleteUserCommand toDeleteUserCommand(UUID userUuid);
}
