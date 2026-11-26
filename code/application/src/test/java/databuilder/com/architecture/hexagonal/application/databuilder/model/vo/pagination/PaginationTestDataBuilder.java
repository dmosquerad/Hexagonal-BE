package com.architecture.hexagonal.application.databuilder.model.vo.pagination;

import com.architecture.hexagonal.application.model.vo.pagination.Pagination;
import lombok.Builder;

@Builder
public class PaginationTestDataBuilder {

  @Builder.Default private Integer page = 1;

  @Builder.Default private Integer size = 10;

  public Pagination pagination() {
    return Pagination.builder().page(page).size(size).build();
  }
}
