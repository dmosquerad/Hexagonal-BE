package com.architecture.hexagonal.application.testutils.usecase.query;

import com.architecture.hexagonal.application.testutils.usecase.common.PaginationTestDataBuilder;
import com.architecture.hexagonal.contract.application.usecase.generated.common.Pagination;
import com.architecture.hexagonal.contract.application.usecase.generated.user.GetUsersFilteredQuery;
import lombok.Builder;

@Builder
public class GetUsersInputTestDataBuilder {

  @Builder.Default
  private String host = "example";

  @Builder.Default
  private Boolean blockEmail = null;

  @Builder.Default
  private Pagination pagination = PaginationTestDataBuilder.builder().build().pagination();

  public GetUsersFilteredQuery getUsersInput() {
    final GetUsersFilteredQuery getUsersFilteredQuery = new GetUsersFilteredQuery();
    getUsersFilteredQuery.setHost(host);
    getUsersFilteredQuery.setBlockEmail(blockEmail);
    getUsersFilteredQuery.setPagination(pagination);

    return getUsersFilteredQuery;
  }
}
