package com.architecture.hexagonal.infrastructure.inbound.cqrs.mapper.user;

import com.architecture.hexagonal.application.user.exists.input.UserExistsQuery;
import com.architecture.hexagonal.infrastructure.contract.cqrs.generated.user.UserExistsQueryDto;
import com.architecture.hexagonal.infrastructure.inbound.cqrs.config.MapstructConfig;
import org.mapstruct.Mapper;

@Mapper(config = MapstructConfig.class)
public interface UserExistsQueryMapper {

  UserExistsQuery toUserExistsQuery(UserExistsQueryDto contract);
}
