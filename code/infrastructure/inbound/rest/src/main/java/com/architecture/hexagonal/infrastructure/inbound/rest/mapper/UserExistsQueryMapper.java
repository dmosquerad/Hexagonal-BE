package com.architecture.hexagonal.infrastructure.inbound.rest.mapper;

import com.architecture.hexagonal.application.input.query.UserExistsQuery;
import com.architecture.hexagonal.infrastructure.inbound.rest.config.MapstructConfig;
import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapstructConfig.class)
public interface UserExistsQueryMapper {

  @Mapping(source = "userUuid", target = "userId")
  UserExistsQuery toUserExistsQuery(UUID userUuid);
}
