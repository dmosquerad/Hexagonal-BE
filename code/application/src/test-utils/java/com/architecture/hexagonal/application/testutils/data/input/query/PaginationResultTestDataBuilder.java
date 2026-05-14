package com.architecture.hexagonal.application.testutils.data.input.query;

import com.architecture.hexagonal.application.cqrs.query.request.pagination.PaginationResult;
import java.util.Collections;
import java.util.Set;
import lombok.Builder;

@Builder
public class PaginationResultTestDataBuilder<T> {

  @Builder.Default
  private Set<T> data = Collections.emptySet();

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
