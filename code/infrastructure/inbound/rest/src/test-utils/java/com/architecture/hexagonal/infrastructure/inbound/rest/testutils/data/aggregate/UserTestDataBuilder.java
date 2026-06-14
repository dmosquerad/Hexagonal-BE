package com.architecture.hexagonal.infrastructure.inbound.rest.testutils.data.aggregate;

import com.architecture.hexagonal.domain.model.aggregate.User;
import com.architecture.hexagonal.domain.model.entity.UserDo;
import com.architecture.hexagonal.domain.model.vo.EmailVo;
import com.architecture.hexagonal.infrastructure.inbound.rest.testutils.data.entity.UserDoTestDataBuilder;
import com.architecture.hexagonal.infrastructure.inbound.rest.testutils.data.vo.EmailVoTestDataBuilder;
import lombok.Builder;

@Builder
public class UserTestDataBuilder {

  @Builder.Default
  private UserDo user = UserDoTestDataBuilder.builder().build().userDo();

  @Builder.Default
  private EmailVo email = EmailVoTestDataBuilder.builder().build().emailVo();

  public User user() {
    return User.builder()
        .user(user)
        .email(email)
        .build();
  }
}
