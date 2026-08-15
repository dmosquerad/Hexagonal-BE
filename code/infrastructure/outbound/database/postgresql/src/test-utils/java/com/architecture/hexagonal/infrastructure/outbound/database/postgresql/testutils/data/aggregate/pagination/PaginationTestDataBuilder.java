package com.architecture.hexagonal.infrastructure.outbound.database.postgresql.testutils.data.aggregate.pagination;

import com.architecture.hexagonal.domain.model.aggregate.pagination.Pagination;
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
