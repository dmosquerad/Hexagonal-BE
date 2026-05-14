package com.architecture.hexagonal.application.cqrs.query.request;

import com.architecture.hexagonal.application.cqrs.query.request.pagination.Pagination;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class GetAllUserQuery {

  String host;
  Boolean blockEmail;
  Pagination pagination;
}