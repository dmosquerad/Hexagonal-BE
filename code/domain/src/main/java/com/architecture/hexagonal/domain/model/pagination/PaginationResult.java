package com.architecture.hexagonal.domain.model.pagination;

import java.util.List;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class PaginationResult<T> {

  List<T> data;
  long totalElements;
  int totalPages;
  int page;
  int size;
}
