package com.architecture.hexagonal.domain.testutils.data.aggregate;

import com.architecture.hexagonal.domain.model.aggregate.user.User;
import com.architecture.hexagonal.domain.model.vo.EmailVo;
import com.architecture.hexagonal.domain.testutils.data.vo.EmailVoTestDataBuilder;
import lombok.Builder;

import java.util.UUID;

@Builder
public class UserTestDataBuilder {

  @Builder.Default
  private UUID userId = UUID.fromString("4059510b-ceb3-4d4c-913e-1759acbd62a4");

  @Builder.Default
  private String name = "Test User";

  @Builder.Default
  private EmailVo email = EmailVoTestDataBuilder.builder().build().emailVo();

  public User user() {
    return User.builder()
        .userId(userId)
        .name(name)
        .email(email)
        .build();
  }
}
