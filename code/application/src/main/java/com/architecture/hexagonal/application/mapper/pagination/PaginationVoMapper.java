package com.architecture.hexagonal.application.mapper.pagination;

import com.architecture.hexagonal.application.model.vo.pagination.Pagination;
import com.architecture.hexagonal.domain.model.vo.pagination.PaginationVo;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PaginationVoMapper {
  PaginationVoMapper INSTANCE = Mappers.getMapper(PaginationVoMapper.class);

  PaginationVo toPaginationVo(Pagination pagination);
}
