package com.architecture.hexagonal.application.cqrs.query.request.pagination;

import jakarta.validation.constraints.Min;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class Pagination {

  @Min(0)
  Integer page;

  @Min(1)
  Integer size;
}
