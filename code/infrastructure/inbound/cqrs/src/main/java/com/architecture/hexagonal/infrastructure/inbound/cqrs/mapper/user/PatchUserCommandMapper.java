package com.architecture.hexagonal.infrastructure.inbound.cqrs.mapper.user;

import com.architecture.hexagonal.application.user.patch.input.PatchUserCommand;
import com.architecture.hexagonal.infrastructure.contract.cqrs.generated.user.PatchUserCommandDto;
import com.architecture.hexagonal.infrastructure.inbound.cqrs.config.MapstructConfig;
import org.mapstruct.Mapper;

@Mapper(config = MapstructConfig.class)
public interface PatchUserCommandMapper {

  PatchUserCommand toPatchUserCommand(PatchUserCommandDto contract);
}
