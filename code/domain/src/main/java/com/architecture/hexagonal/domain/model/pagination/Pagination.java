package com.architecture.hexagonal.domain.model.pagination;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class Pagination {

  Integer page;
  Integer size;
}
