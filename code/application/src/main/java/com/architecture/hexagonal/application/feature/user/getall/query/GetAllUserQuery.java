package com.architecture.hexagonal.application.feature.user.getall.query;

import com.architecture.hexagonal.application.common.pagination.Pagination;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class GetAllUserQuery {
  String host;
  Boolean blockEmail;
  Pagination pagination;
}
