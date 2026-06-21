package com.architecture.hexagonal.infrastructure.inbound.rest.mapper.user;

import com.architecture.hexagonal.infrastructure.contract.orchestration.generated.user.PatchUserCommandDto;
import com.architecture.hexagonal.infrastructure.contract.rest.user.server.dto.UserPatchDto;
import com.architecture.hexagonal.infrastructure.inbound.rest.config.MapstructConfig;
import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapstructConfig.class)
public interface PatchUserCommandDtoMapper {

  @Mapping(source = "userUuid", target = "userId")
  @Mapping(source = "userPatchDto.name", target = "name")
  @Mapping(source = "userPatchDto.email", target = "email")
  PatchUserCommandDto toPatchUserCommand(UUID userUuid, UserPatchDto userPatchDto);
}
