package com.architecture.hexagonal.application.testutils.data.pagination;

import com.architecture.hexagonal.domain.model.pagination.Pagination;
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
