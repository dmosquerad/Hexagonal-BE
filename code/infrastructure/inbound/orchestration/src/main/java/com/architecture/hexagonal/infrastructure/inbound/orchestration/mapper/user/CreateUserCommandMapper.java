package com.architecture.hexagonal.infrastructure.inbound.orchestration.mapper.user;

import com.architecture.hexagonal.application.business.user.create.input.CreateUserInput;
import com.architecture.hexagonal.infrastructure.inbound.contract.orchestration.generated.user.CreateUserCommandDto;
import com.architecture.hexagonal.infrastructure.inbound.orchestration.config.mapstruct.MapstructConfig;
import org.mapstruct.Mapper;

@Mapper(config = MapstructConfig.class)
public interface CreateUserCommandMapper {

  CreateUserInput toCreateUserCommand(CreateUserCommandDto contract);
}
