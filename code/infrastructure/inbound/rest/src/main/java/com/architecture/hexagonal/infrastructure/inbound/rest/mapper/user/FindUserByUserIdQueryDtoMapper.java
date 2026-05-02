package com.architecture.hexagonal.infrastructure.inbound.rest.mapper.user;

import com.architecture.hexagonal.infrastructure.contract.cqrs.generated.user.FindUserByUserIdQueryDto;
import com.architecture.hexagonal.infrastructure.inbound.rest.config.MapstructConfig;
import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapstructConfig.class)
public interface FindUserByUserIdQueryDtoMapper {

  @Mapping(source = "userUuid", target = "userId")
  FindUserByUserIdQueryDto toFindUserByUserIdQuery(UUID userUuid);
}
