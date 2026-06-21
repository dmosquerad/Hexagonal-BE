package com.architecture.hexagonal.infrastructure.inbound.orchestration.testutils.data.query;

import com.architecture.hexagonal.infrastructure.contract.orchestration.generated.user.UserExistsQueryDto;
import lombok.Builder;

import java.util.UUID;

@Builder
public class UserExistsQueryDtoTestDataBuilder {

  @Builder.Default
  private UUID userId = UUID.randomUUID();

  public UserExistsQueryDto userExistsQueryDto() {
    UserExistsQueryDto dto = new UserExistsQueryDto();
    dto.setUserId(userId);
    return dto;
  }
}
