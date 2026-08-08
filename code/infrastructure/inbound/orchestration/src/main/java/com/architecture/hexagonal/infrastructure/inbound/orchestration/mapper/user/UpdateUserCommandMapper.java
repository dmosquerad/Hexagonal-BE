package com.architecture.hexagonal.infrastructure.inbound.orchestration.mapper.user;

import com.architecture.hexagonal.application.business.user.update.input.UpdateUserInput;
import com.architecture.hexagonal.infrastructure.inbound.contract.orchestration.generated.user.UpdateUserCommandDto;
import com.architecture.hexagonal.infrastructure.inbound.orchestration.config.mapstruct.MapstructConfig;
import org.mapstruct.Mapper;

@Mapper(config = MapstructConfig.class)
public interface UpdateUserCommandMapper {

  UpdateUserInput toUpdateUserCommand(UpdateUserCommandDto contract);
}
