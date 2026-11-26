package com.architecture.hexagonal.infrastructure.inbound.endpoint.rest.mapper.user;

import com.architecture.hexagonal.application.model.query.user.FindUserByUserIdQuery;
import com.architecture.hexagonal.infrastructure.inbound.endpoint.rest.config.mapstruct.MapstructConfig;
import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapstructConfig.class)
public interface FindUserByUserIdQueryDtoMapper {

  @Mapping(source = "userUuid", target = "userId")
  FindUserByUserIdQuery toFindUserByUserIdQuery(UUID userUuid);
}
