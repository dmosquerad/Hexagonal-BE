package com.architecture.hexagonal.infrastructure.inbound.rest.mapper.user;

import com.architecture.hexagonal.contract.application.usecase.generated.user.UpdateUserCommand;
import com.architecture.hexagonal.contract.inbound.rest.user.server.dto.UserUpdateDto;
import com.architecture.hexagonal.infrastructure.inbound.rest.config.mapstruct.MapstructConfig;
import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapstructConfig.class)
public interface UpdateUserCommandDtoMapper {

  @Mapping(source = "userUuid", target = "userId")
  @Mapping(source = "userUpdateDto.name", target = "name")
  @Mapping(source = "userUpdateDto.email", target = "email")
  UpdateUserCommand toUpdateUserCommand(UUID userUuid, UserUpdateDto userUpdateDto);
}
