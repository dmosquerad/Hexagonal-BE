package com.architecture.hexagonal.infrastructure.inbound.rest.mapper;

import com.architecture.hexagonal.application.feature.user.getall.query.GetAllUserQuery;
import com.architecture.hexagonal.application.common.pagination.Pagination;
import com.architecture.hexagonal.infrastructure.inbound.rest.config.MapstructConfig;
import org.mapstruct.Mapper;

@Mapper(config = MapstructConfig.class)
public interface GetAllUserQueryMapper {

  GetAllUserQuery toGetAllUserQuery(
      String host,
      Boolean blockEmail,
      Pagination pagination);
}
