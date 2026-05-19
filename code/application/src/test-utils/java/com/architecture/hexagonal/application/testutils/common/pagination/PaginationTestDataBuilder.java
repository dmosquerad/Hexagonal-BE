package com.architecture.hexagonal.application.testutils.common.pagination;

import com.architecture.hexagonal.application.common.pagination.Pagination;
import lombok.Builder;

@Builder
public class PaginationTestDataBuilder {

  @Builder.Default
  private Integer page = 0;

  @Builder.Default
  private Integer size = 100;

  public Pagination pagination() {
    return Pagination.builder()
        .page(page)
        .size(size)
        .build();
  }
}
