package com.architecture.hexagonal.infrastructure.inbound.orchestration.mapper.pagination;

import com.architecture.hexagonal.domain.model.pagination.Pagination;
import com.architecture.hexagonal.infrastructure.contract.orchestration.generated.common.PaginationDto;
import com.architecture.hexagonal.infrastructure.inbound.orchestration.config.MapstructConfig;
import org.mapstruct.Mapper;

@Mapper(config = MapstructConfig.class)
public interface PaginationMapper {

  Pagination toPagination(PaginationDto pagination);
}
