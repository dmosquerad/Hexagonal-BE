package com.architecture.hexagonal.infrastructure.inbound.cqrs.testutils.data.aggregate;

import com.architecture.hexagonal.domain.model.aggregate.User;
import com.architecture.hexagonal.domain.model.entity.UserDo;
import com.architecture.hexagonal.domain.model.vo.EmailVo;
import lombok.Builder;

import java.util.UUID;

@Builder
public class UserTestDataBuilder {

  @Builder.Default
  private UserDo user = UserDo.builder()
      .userId(UUID.randomUUID())
      .name("Test User")
      .build();

  @Builder.Default
  private EmailVo email = EmailVo.builder()
      .username("test")
      .host("example")
      .tld("com")
      .build();

  public User user() {
    return User.builder()
        .user(user)
        .email(email)
        .build();
  }
}
