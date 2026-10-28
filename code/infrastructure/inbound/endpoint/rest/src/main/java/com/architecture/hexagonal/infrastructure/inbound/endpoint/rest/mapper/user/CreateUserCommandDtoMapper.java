package com.architecture.hexagonal.infrastructure.inbound.endpoint.rest.mapper.user;

import com.architecture.hexagonal.contract.bus.model.user.command.CreateUserCommand;
import com.architecture.hexagonal.contract.inbound.endpoint.rest.user.server.dto.UserCreateDto;
import com.architecture.hexagonal.infrastructure.inbound.endpoint.rest.config.mapstruct.MapstructConfig;
import org.mapstruct.Mapper;

@Mapper(config = MapstructConfig.class)
public interface CreateUserCommandDtoMapper {

  CreateUserCommand toCreateUserCommand(UserCreateDto userCreateDto);
}
