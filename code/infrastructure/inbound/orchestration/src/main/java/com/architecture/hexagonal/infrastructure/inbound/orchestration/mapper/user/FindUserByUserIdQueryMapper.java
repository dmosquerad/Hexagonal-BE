package com.architecture.hexagonal.infrastructure.inbound.orchestration.mapper.user;

import com.architecture.hexagonal.application.business.user.findbyid.input.FindUserByUserIdQuery;
import com.architecture.hexagonal.infrastructure.contract.orchestration.generated.user.FindUserByUserIdQueryDto;
import com.architecture.hexagonal.infrastructure.inbound.orchestration.config.MapstructConfig;
import org.mapstruct.Mapper;

@Mapper(config = MapstructConfig.class)
public interface FindUserByUserIdQueryMapper {

  FindUserByUserIdQuery toFindUserByUserIdQuery(FindUserByUserIdQueryDto contract);
}
