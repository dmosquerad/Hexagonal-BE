package com.architecture.hexagonal.domain.model.pagination;

import java.util.Set;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class PaginationResult<T> {

  Set<T> data;
  long totalElements;
  int totalPages;
  int page;
  int size;
}
