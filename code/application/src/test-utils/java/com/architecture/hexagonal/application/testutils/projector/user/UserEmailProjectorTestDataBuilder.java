package com.architecture.hexagonal.application.testutils.projector.user;

import com.architecture.hexagonal.application.projector.user.UserEmailProjector;
import com.architecture.hexagonal.domain.model.vo.email.EmailBlockRulesVo;
import com.architecture.hexagonal.domain.testutils.data.model.vo.email.EmailBlockRulesVoTestDataBuilder;
import lombok.Builder;

@Builder
public class UserEmailProjectorTestDataBuilder {

  @Builder.Default private String host = "example";

  @Builder.Default private Boolean blockEmail = false;

  @Builder.Default
  private EmailBlockRulesVo blockedRules = EmailBlockRulesVoTestDataBuilder.builder().build().emailBlockRulesVo();

  public UserEmailProjector userEmailProjector() {
    return UserEmailProjector.builder()
        .host(host)
        .blockEmail(blockEmail)
        .blockedRules(blockedRules)
        .build();
  }
}
