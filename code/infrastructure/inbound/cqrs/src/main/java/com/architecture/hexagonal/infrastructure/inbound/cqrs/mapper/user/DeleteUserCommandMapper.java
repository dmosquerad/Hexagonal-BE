package com.architecture.hexagonal.infrastructure.inbound.cqrs.mapper.user;

import com.architecture.hexagonal.application.user.delete.input.DeleteUserCommand;
import com.architecture.hexagonal.infrastructure.contract.cqrs.generated.user.DeleteUserCommandDto;
import com.architecture.hexagonal.infrastructure.inbound.cqrs.config.MapstructConfig;
import org.mapstruct.Mapper;

@Mapper(config = MapstructConfig.class)
public interface DeleteUserCommandMapper {

  DeleteUserCommand toDeleteUserCommand(DeleteUserCommandDto contract);
}
