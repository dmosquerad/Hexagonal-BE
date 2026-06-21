package com.architecture.hexagonal.infrastructure.inbound.orchestration.mapper.user;

import com.architecture.hexagonal.application.business.user.exists.input.UserExistsQuery;
import com.architecture.hexagonal.infrastructure.contract.orchestration.generated.user.UserExistsQueryDto;
import com.architecture.hexagonal.infrastructure.inbound.orchestration.config.MapstructConfig;
import org.mapstruct.Mapper;

@Mapper(config = MapstructConfig.class)
public interface UserExistsQueryMapper {

  UserExistsQuery toUserExistsQuery(UserExistsQueryDto contract);
}
