package com.architecture.hexagonal.infrastructure.inbound.orchestration.testutils.data.query;

import com.architecture.hexagonal.infrastructure.contract.orchestration.generated.user.FindUserByUserIdQueryDto;
import lombok.Builder;

import java.util.UUID;

@Builder
public class FindUserByUserIdQueryDtoTestDataBuilder {

  @Builder.Default
  private UUID userId = UUID.randomUUID();

  public FindUserByUserIdQueryDto findUserByUserIdQueryDto() {
    FindUserByUserIdQueryDto dto = new FindUserByUserIdQueryDto();
    dto.setUserId(userId);
    return dto;
  }
}
