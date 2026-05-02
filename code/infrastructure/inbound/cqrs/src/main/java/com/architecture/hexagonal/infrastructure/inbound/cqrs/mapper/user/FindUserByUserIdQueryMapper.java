package com.architecture.hexagonal.infrastructure.inbound.cqrs.mapper.user;

import com.architecture.hexagonal.application.user.findbyid.input.FindUserByUserIdQuery;
import com.architecture.hexagonal.infrastructure.contract.cqrs.generated.user.FindUserByUserIdQueryDto;
import com.architecture.hexagonal.infrastructure.inbound.cqrs.config.MapstructConfig;
import org.mapstruct.Mapper;

@Mapper(config = MapstructConfig.class)
public interface FindUserByUserIdQueryMapper {

  FindUserByUserIdQuery toFindUserByUserIdQuery(FindUserByUserIdQueryDto contract);
}
