package com.architecture.hexagonal.infrastructure.inbound.rest.mapper.user;

import com.architecture.hexagonal.domain.model.pagination.Pagination;
import com.architecture.hexagonal.infrastructure.contract.orchestration.generated.user.GetUsersFilteredQueryDto;
import com.architecture.hexagonal.infrastructure.inbound.rest.config.MapstructConfig;
import org.mapstruct.Mapper;

@Mapper(config = MapstructConfig.class)
public interface GetAllUserQueryDtoMapper {

  GetUsersFilteredQueryDto toGetAllUserQuery(
      String host, Boolean blockEmail, Pagination pagination);
}
