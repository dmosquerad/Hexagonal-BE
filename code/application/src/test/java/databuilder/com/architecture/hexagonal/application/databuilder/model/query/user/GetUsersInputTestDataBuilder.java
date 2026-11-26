package com.architecture.hexagonal.application.databuilder.model.query.user;

import com.architecture.hexagonal.application.databuilder.model.vo.pagination.PaginationTestDataBuilder;
import com.architecture.hexagonal.application.model.vo.pagination.Pagination;
import com.architecture.hexagonal.application.model.query.user.GetUsersFilteredQuery;
import lombok.Builder;

@Builder
public class GetUsersInputTestDataBuilder {

  @Builder.Default private String host = "example";

  @Builder.Default private Boolean blockEmail = null;

  @Builder.Default
  private Pagination pagination = PaginationTestDataBuilder.builder().build().pagination();

  public GetUsersFilteredQuery getUsersInput() {
    return GetUsersFilteredQuery.builder()
        .host(host)
        .blockEmail(blockEmail)
        .pagination(pagination)
        .build();
  }
}
