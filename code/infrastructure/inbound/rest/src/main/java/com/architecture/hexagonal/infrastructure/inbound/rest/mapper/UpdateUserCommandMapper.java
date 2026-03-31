package com.architecture.hexagonal.infrastructure.inbound.rest.mapper;

import com.architecture.hexagonal.application.input.command.UpdateUserCommand;
import com.architecture.hexagonal.infrastructure.inbound.rest.config.MapstructConfig;
import com.architecture.hexagonal.infrastructure.inbound.rest.dto.UserUpdateDto;
import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapstructConfig.class)
public interface UpdateUserCommandMapper {

  @Mapping(source = "userUuid", target = "userId")
  @Mapping(source = "userUpdateDto.name", target = "name")
  @Mapping(source = "userUpdateDto.email", target = "email")
  UpdateUserCommand toUpdateUserCommand(UUID userUuid, UserUpdateDto userUpdateDto);
}
