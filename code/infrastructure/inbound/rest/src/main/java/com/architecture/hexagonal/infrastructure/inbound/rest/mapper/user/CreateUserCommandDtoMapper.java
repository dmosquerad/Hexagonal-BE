package com.architecture.hexagonal.infrastructure.inbound.rest.mapper.user;

import com.architecture.hexagonal.contract.application.generated.user.CreateUserCommand;
import com.architecture.hexagonal.contract.inbound.rest.user.server.dto.UserCreateDto;
import com.architecture.hexagonal.infrastructure.inbound.rest.config.mapstruct.MapstructConfig;
import org.mapstruct.Mapper;

@Mapper(config = MapstructConfig.class)
public interface CreateUserCommandDtoMapper {

  CreateUserCommand toCreateUserCommand(UserCreateDto userCreateDto);
}
