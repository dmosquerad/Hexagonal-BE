package com.architecture.hexagonal.infrastructure.inbound.endpoint.rest.mapper.user;

import com.architecture.hexagonal.contract.bus.model.user.command.PatchUserCommand;
import com.architecture.hexagonal.contract.inbound.endpoint.rest.user.server.dto.UserPatchDto;
import com.architecture.hexagonal.infrastructure.inbound.endpoint.rest.config.mapstruct.MapstructConfig;
import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapstructConfig.class)
public interface PatchUserCommandDtoMapper {

  @Mapping(source = "userUuid", target = "userId")
  @Mapping(source = "userPatchDto.name", target = "name")
  @Mapping(source = "userPatchDto.email", target = "email")
  PatchUserCommand toPatchUserCommand(UUID userUuid, UserPatchDto userPatchDto);
}
