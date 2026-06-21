package com.architecture.hexagonal.infrastructure.inbound.orchestration.mapper.user;

import com.architecture.hexagonal.application.business.user.update.input.UpdateUserCommand;
import com.architecture.hexagonal.infrastructure.contract.orchestration.generated.user.UpdateUserCommandDto;
import com.architecture.hexagonal.infrastructure.inbound.orchestration.config.MapstructConfig;
import org.mapstruct.Mapper;

@Mapper(config = MapstructConfig.class)
public interface UpdateUserCommandMapper {

  UpdateUserCommand toUpdateUserCommand(UpdateUserCommandDto contract);
}
