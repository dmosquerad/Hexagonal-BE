package com.architecture.hexagonal.application.testutils.data.input.query;

import com.architecture.hexagonal.application.input.query.GetAllUserQuery;
import lombok.Builder;

@Builder
public class GetAllUserQueryTestDataBuilder {

  @Builder.Default
  private String host = "example";

  @Builder.Default
  private Boolean blockHost = true;

  public GetAllUserQuery getAllUserQuery() {
    return GetAllUserQuery.builder()
        .host(host)
        .blockHost(blockHost)
        .build();
  }
}
