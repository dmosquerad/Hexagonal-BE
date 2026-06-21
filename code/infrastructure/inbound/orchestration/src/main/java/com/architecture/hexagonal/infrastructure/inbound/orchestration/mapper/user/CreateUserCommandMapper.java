package com.architecture.hexagonal.infrastructure.inbound.orchestration.mapper.user;

import com.architecture.hexagonal.application.business.user.create.input.CreateUserCommand;
import com.architecture.hexagonal.infrastructure.contract.orchestration.generated.user.CreateUserCommandDto;
import com.architecture.hexagonal.infrastructure.inbound.orchestration.config.MapstructConfig;
import org.mapstruct.Mapper;

@Mapper(config = MapstructConfig.class)
public interface CreateUserCommandMapper {

  CreateUserCommand toCreateUserCommand(CreateUserCommandDto contract);
}
