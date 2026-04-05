package com.architecture.hexagonal.application.testutils.data.input.query;

import com.architecture.hexagonal.application.cqrs.query.request.GetAllUserQuery;
import lombok.Builder;

@Builder
public class GetAllUserQueryTestDataBuilder {

  @Builder.Default
  private String host = "example";

  @Builder.Default
  private Boolean blockEmail = null;

  public GetAllUserQuery getAllUserQuery() {
    return GetAllUserQuery.builder()
        .host(host)
        .blockEmail(blockEmail)
        .build();
  }
}
