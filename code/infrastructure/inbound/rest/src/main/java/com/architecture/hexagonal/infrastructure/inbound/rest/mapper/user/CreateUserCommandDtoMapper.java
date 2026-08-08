package com.architecture.hexagonal.infrastructure.inbound.rest.mapper.user;

import com.architecture.hexagonal.infrastructure.inbound.contract.orchestration.generated.user.CreateUserCommandDto;
import com.architecture.hexagonal.infrastructure.inbound.contract.rest.user.server.dto.UserCreateDto;
import com.architecture.hexagonal.infrastructure.inbound.rest.config.mapstruct.MapstructConfig;
import org.mapstruct.Mapper;

@Mapper(config = MapstructConfig.class)
public interface CreateUserCommandDtoMapper {

  CreateUserCommandDto toCreateUserCommand(UserCreateDto userCreateDto);
}
