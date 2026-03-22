package com.architecture.hexagonal.infrastructure.inbound.rest.testutils.data.entity;

import com.architecture.hexagonal.domain.data.entity.User;
import com.architecture.hexagonal.domain.data.vo.EmailVo;
import java.util.UUID;
import lombok.Builder;

@Builder
public class UserTestDataBuilder {

  @Builder.Default
  private UUID userId = UUID.fromString("4059510b-ceb3-4d4c-913e-1759acbd62a4");

  @Builder.Default
  private EmailVo email = EmailVo.builder()
      .username("test")
      .host("example")
      .tld("com")
      .build();

  @Builder.Default
  private String name = "Test User";

  public User user() {
    return User.builder()
        .userId(userId)
        .email(email)
        .name(name)
        .build();
  }
}
