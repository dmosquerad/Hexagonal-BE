package com.architecture.hexagonal.infrastructure.inbound.rest.mapper;

import com.architecture.hexagonal.application.feature.user.findbyid.query.FindUserByUserIdQuery;
import com.architecture.hexagonal.infrastructure.inbound.rest.config.MapstructConfig;
import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapstructConfig.class)
public interface FindUserByUserIdQueryMapper {

  @Mapping(source = "userUuid", target = "userId")
  FindUserByUserIdQuery toFindUserByUserIdQuery(UUID userUuid);
}
