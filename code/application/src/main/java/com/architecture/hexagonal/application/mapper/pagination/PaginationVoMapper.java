package com.architecture.hexagonal.application.mapper.pagination;

import com.architecture.hexagonal.application.config.mapstruct.MapstructConfig;
import com.architecture.hexagonal.contract.bus.model.common.Pagination;
import com.architecture.hexagonal.domain.model.vo.pagination.PaginationVo;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(config = MapstructConfig.class)
public interface PaginationVoMapper {

  PaginationVoMapper INSTANCE = Mappers.getMapper(PaginationVoMapper.class);

  PaginationVo toPaginationVo(Pagination pagination);
}
