package com.architecture.hexagonal.application.feature.user.getallfiltered.query;

import com.architecture.hexagonal.application.common.pagination.Pagination;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class GetUsersFilteredQuery {
  String host;
  Boolean blockEmail;
  Pagination pagination;
}
