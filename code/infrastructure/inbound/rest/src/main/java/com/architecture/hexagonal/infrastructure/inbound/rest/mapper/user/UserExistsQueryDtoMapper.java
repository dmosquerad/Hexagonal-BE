package com.architecture.hexagonal.infrastructure.inbound.rest.mapper.user;

import com.architecture.hexagonal.infrastructure.contract.cqrs.generated.user.UserExistsQueryDto;
import com.architecture.hexagonal.infrastructure.inbound.rest.config.MapstructConfig;
import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapstructConfig.class)
public interface UserExistsQueryDtoMapper {

  @Mapping(source = "userUuid", target = "userId")
  UserExistsQueryDto toUserExistsQuery(UUID userUuid);
}
