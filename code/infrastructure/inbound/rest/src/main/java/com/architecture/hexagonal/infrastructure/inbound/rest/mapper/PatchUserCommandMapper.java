package com.architecture.hexagonal.infrastructure.inbound.rest.mapper;

import com.architecture.hexagonal.application.feature.user.patch.command.PatchUserCommand;
import com.architecture.hexagonal.infrastructure.inbound.rest.config.MapstructConfig;
import com.architecture.hexagonal.infrastructure.contract.rest.user.server.dto.UserPatchDto;
import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapstructConfig.class)
public interface PatchUserCommandMapper {

  @Mapping(source = "userUuid", target = "userId")
  @Mapping(source = "userPatchDto.name", target = "name")
  @Mapping(source = "userPatchDto.email", target = "email")
  PatchUserCommand toPatchUserCommand(UUID userUuid, UserPatchDto userPatchDto);
}
