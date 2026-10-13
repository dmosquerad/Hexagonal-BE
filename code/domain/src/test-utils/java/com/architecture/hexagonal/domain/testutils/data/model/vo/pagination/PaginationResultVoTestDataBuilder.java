package com.architecture.hexagonal.domain.testutils.data.model.vo.pagination;

import com.architecture.hexagonal.domain.model.vo.pagination.PaginationResultVo;
import java.util.Collections;
import java.util.List;
import lombok.Builder;

@Builder
public class PaginationResultVoTestDataBuilder<T> {

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

  public PaginationResultVo<T> paginationResult() {
    return PaginationResultVo.<T>builder()
        .data(data)
        .totalElements(totalElements)
        .totalPages(totalPages)
        .page(page)
        .size(size)
        .build();
  }
}
