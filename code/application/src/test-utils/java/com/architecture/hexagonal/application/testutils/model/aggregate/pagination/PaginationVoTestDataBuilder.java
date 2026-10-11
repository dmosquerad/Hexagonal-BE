package com.architecture.hexagonal.application.testutils.model.aggregate.pagination;

import com.architecture.hexagonal.domain.model.vo.pagination.PaginationVo;
import lombok.Builder;

@Builder
public class PaginationVoTestDataBuilder {

  @Builder.Default
  private Integer page = 0;

  @Builder.Default
  private Integer size = 100;

  public PaginationVo pagination() {
    return PaginationVo.builder()
        .page(page)
        .size(size)
        .build();
  }
}
