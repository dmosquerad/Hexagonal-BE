package com.architecture.hexagonal.infrastructure.inbound.rest.mapper;

import com.architecture.hexagonal.application.input.command.CreateUserCommand;
import com.architecture.hexagonal.infrastructure.inbound.rest.config.MapstructConfig;
import com.architecture.hexagonal.infrastructure.inbound.rest.dto.UserCreateDto;
import org.mapstruct.Mapper;

@Mapper(config = MapstructConfig.class)
public interface CreateUserCommandMapper {

  CreateUserCommand toCreateUserCommand(UserCreateDto userCreateDto);
}
