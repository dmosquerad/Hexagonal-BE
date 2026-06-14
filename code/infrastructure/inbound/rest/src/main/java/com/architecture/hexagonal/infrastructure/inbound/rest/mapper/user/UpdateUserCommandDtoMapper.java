package com.architecture.hexagonal.infrastructure.inbound.rest.mapper.user;

import com.architecture.hexagonal.infrastructure.contract.cqrs.generated.user.UpdateUserCommandDto;
import com.architecture.hexagonal.infrastructure.contract.rest.user.server.dto.UserUpdateDto;
import com.architecture.hexagonal.infrastructure.inbound.rest.config.MapstructConfig;
import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapstructConfig.class)
public interface UpdateUserCommandDtoMapper {

  @Mapping(source = "userUuid", target = "userId")
  @Mapping(source = "userUpdateDto.name", target = "name")
  @Mapping(source = "userUpdateDto.email", target = "email")
  UpdateUserCommandDto toUpdateUserCommand(UUID userUuid, UserUpdateDto userUpdateDto);
}
