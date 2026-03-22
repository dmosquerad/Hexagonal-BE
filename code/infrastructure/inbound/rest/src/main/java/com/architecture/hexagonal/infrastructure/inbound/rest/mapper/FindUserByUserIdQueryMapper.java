package com.architecture.hexagonal.infrastructure.inbound.rest.mapper;

import com.architecture.hexagonal.application.input.query.FindUserByUserIdQuery;
import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface FindUserByUserIdQueryMapper {

  @Mapping(source = "userUuid", target = "userId")
  FindUserByUserIdQuery toFindUserByUserIdQuery(UUID userUuid);
}
