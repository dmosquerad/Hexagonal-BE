package com.architecture.hexagonal.application.testutils.data.input.query;

import com.architecture.hexagonal.application.feature.user.getall.query.GetAllUserQuery;
import com.architecture.hexagonal.application.common.pagination.Pagination;
import lombok.Builder;

@Builder
public class GetAllUserQueryTestDataBuilder {

  @Builder.Default
  private String host = "example";

  @Builder.Default
  private Boolean blockEmail = null;

  @Builder.Default
  private Pagination pagination = PaginationTestDataBuilder.builder().build().pagination();

  public GetAllUserQuery getAllUserQuery() {
    return GetAllUserQuery.builder()
        .host(host)
        .blockEmail(blockEmail)
        .pagination(pagination)
        .build();
  }
}
