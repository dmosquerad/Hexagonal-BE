package com.architecture.hexagonal.infrastructure.inbound.rest.mapper.user;

import com.architecture.hexagonal.contract.bus.model.user.GetUsersFilteredQuery;
import com.architecture.hexagonal.domain.model.vo.pagination.PaginationVo;
import com.architecture.hexagonal.infrastructure.inbound.rest.config.mapstruct.MapstructConfig;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapstructConfig.class)
public interface GetAllUserQueryDtoMapper {

  @Mapping(source = "paginationVo", target = "pagination")
  GetUsersFilteredQuery toGetAllUserQuery(
      final String host, final Boolean blockEmail, final PaginationVo paginationVo);
}
