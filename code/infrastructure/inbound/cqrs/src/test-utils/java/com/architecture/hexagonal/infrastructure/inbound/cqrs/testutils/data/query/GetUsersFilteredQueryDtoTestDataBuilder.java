package com.architecture.hexagonal.infrastructure.inbound.cqrs.testutils.data.query;

import com.architecture.hexagonal.infrastructure.contract.cqrs.generated.common.PaginationDto;
import com.architecture.hexagonal.infrastructure.contract.cqrs.generated.user.GetUsersFilteredQueryDto;
import lombok.Builder;

@Builder
public class GetUsersFilteredQueryDtoTestDataBuilder {

  @Builder.Default
  private String host = "example";

  @Builder.Default
  private Boolean blockEmail = null;

  @Builder.Default
  private PaginationDto pagination = null;

  public GetUsersFilteredQueryDto getUsersFilteredQueryDto() {
    GetUsersFilteredQueryDto dto = new GetUsersFilteredQueryDto();
    dto.setHost(host);
    dto.setBlockEmail(blockEmail);
    dto.setPagination(pagination);
    return dto;
  }
}
