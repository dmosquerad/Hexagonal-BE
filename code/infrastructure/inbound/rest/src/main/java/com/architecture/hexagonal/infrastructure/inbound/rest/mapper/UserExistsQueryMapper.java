package com.architecture.hexagonal.infrastructure.inbound.rest.mapper;

import com.architecture.hexagonal.application.input.query.UserExistsQuery;
import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface UserExistsQueryMapper {

  @Mapping(source = "userUuid", target = "userId")
  UserExistsQuery toUserExistsQuery(UUID userUuid);
}
