package com.architecture.hexagonal.infrastructure.inbound.orchestration.mapper.user;

import com.architecture.hexagonal.application.business.user.delete.input.DeleteUserCommand;
import com.architecture.hexagonal.infrastructure.contract.orchestration.generated.user.DeleteUserCommandDto;
import com.architecture.hexagonal.infrastructure.inbound.orchestration.config.MapstructConfig;
import org.mapstruct.Mapper;

@Mapper(config = MapstructConfig.class)
public interface DeleteUserCommandMapper {

  DeleteUserCommand toDeleteUserCommand(DeleteUserCommandDto contract);
}
