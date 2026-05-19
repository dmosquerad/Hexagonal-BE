package com.architecture.hexagonal.infrastructure.inbound.rest.mapper;

import com.architecture.hexagonal.application.feature.user.getallfiltered.query.GetUsersFilteredQuery;
import com.architecture.hexagonal.application.common.pagination.Pagination;
import com.architecture.hexagonal.infrastructure.inbound.rest.config.MapstructConfig;
import org.mapstruct.Mapper;

@Mapper(config = MapstructConfig.class)
public interface GetAllUserQueryMapper {

  GetUsersFilteredQuery toGetAllUserQuery(
      String host,
      Boolean blockEmail,
      Pagination pagination);
}
