package com.architecture.hexagonal.infrastructure.inbound.cqrs.mapper.user;

import com.architecture.hexagonal.application.user.update.input.UpdateUserCommand;
import com.architecture.hexagonal.infrastructure.contract.cqrs.generated.user.UpdateUserCommandDto;
import com.architecture.hexagonal.infrastructure.inbound.cqrs.config.MapstructConfig;
import org.mapstruct.Mapper;

@Mapper(config = MapstructConfig.class)
public interface UpdateUserCommandMapper {

  UpdateUserCommand toUpdateUserCommand(UpdateUserCommandDto contract);
}
