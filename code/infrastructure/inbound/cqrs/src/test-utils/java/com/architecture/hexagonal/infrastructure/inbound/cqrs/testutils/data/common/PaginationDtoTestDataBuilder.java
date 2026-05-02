package com.architecture.hexagonal.infrastructure.inbound.cqrs.testutils.data.common;

import com.architecture.hexagonal.infrastructure.contract.cqrs.generated.common.PaginationDto;
import lombok.Builder;

@Builder
public class PaginationDtoTestDataBuilder {

  @Builder.Default
  private Integer page = 0;

  @Builder.Default
  private Integer size = 10;

  public PaginationDto paginationDto() {
    PaginationDto dto = new PaginationDto();
    dto.setPage(page);
    dto.setSize(size);
    return dto;
  }
}
