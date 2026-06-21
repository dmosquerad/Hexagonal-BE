package com.architecture.hexagonal.infrastructure.outbound.database.testutils.data.pagination;

import com.architecture.hexagonal.domain.model.pagination.PaginationResult;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import lombok.Builder;

@Builder
public class PaginationResultTestDataBuilder<T> {

  @Builder.Default
  private List<T> data = Collections.emptyList();

  @Builder.Default
  private long totalElements = 1L;

  @Builder.Default
  private int totalPages = 1;

  @Builder.Default
  private int page = 0;

  @Builder.Default
  private int size = 100;

  public PaginationResult<T> paginationResult() {
    return PaginationResult.<T>builder()
        .data(data)
        .totalElements(totalElements)
        .totalPages(totalPages)
        .page(page)
        .size(size)
        .build();
  }
}
