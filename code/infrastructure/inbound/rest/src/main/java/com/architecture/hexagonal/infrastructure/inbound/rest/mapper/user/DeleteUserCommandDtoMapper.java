package com.architecture.hexagonal.infrastructure.inbound.rest.mapper.user;

import com.architecture.hexagonal.infrastructure.contract.cqrs.generated.user.DeleteUserCommandDto;
import com.architecture.hexagonal.infrastructure.inbound.rest.config.MapstructConfig;
import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapstructConfig.class)
public interface DeleteUserCommandDtoMapper {

  @Mapping(source = "userUuid", target = "userId")
  DeleteUserCommandDto toDeleteUserCommand(UUID userUuid);
}
