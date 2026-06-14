package com.architecture.hexagonal.infrastructure.inbound.cqrs.mapper.user;

import com.architecture.hexagonal.application.user.create.input.CreateUserCommand;
import com.architecture.hexagonal.infrastructure.contract.cqrs.generated.user.CreateUserCommandDto;
import com.architecture.hexagonal.infrastructure.inbound.cqrs.config.MapstructConfig;
import org.mapstruct.Mapper;

@Mapper(config = MapstructConfig.class)
public interface CreateUserCommandMapper {

  CreateUserCommand toCreateUserCommand(CreateUserCommandDto contract);
}
