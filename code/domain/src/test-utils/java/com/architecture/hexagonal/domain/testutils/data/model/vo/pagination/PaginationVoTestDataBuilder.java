package com.architecture.hexagonal.domain.testutils.data.model.vo.pagination;

import com.architecture.hexagonal.domain.model.vo.pagination.PaginationVo;
import lombok.Builder;

@Builder
public class PaginationVoTestDataBuilder {

  @Builder.Default
  private Integer page = 0;

  @Builder.Default
  private Integer size = 100;

  public PaginationVo paginationVo() {
    return PaginationVo.builder()
        .page(page)
        .size(size)
        .build();
  }
}
