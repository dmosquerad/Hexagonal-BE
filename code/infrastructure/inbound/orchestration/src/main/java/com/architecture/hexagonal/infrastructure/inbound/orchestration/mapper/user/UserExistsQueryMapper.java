package com.architecture.hexagonal.infrastructure.inbound.orchestration.mapper.user;

import com.architecture.hexagonal.application.business.user.exists.input.UserExistsInput;
import com.architecture.hexagonal.infrastructure.inbound.contract.orchestration.generated.user.UserExistsQueryDto;
import com.architecture.hexagonal.infrastructure.inbound.orchestration.config.mapstruct.MapstructConfig;
import org.mapstruct.Mapper;

@Mapper(config = MapstructConfig.class)
public interface UserExistsQueryMapper {

  UserExistsInput toUserExistsQuery(UserExistsQueryDto contract);
}
