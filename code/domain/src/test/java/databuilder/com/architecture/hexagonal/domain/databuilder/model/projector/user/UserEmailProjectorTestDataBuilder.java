package com.architecture.hexagonal.domain.databuilder.model.projector.user;

import com.architecture.hexagonal.domain.databuilder.model.vo.email.EmailBlockRulesVoTestDataBuilder;
import com.architecture.hexagonal.domain.model.projector.user.UserEmailProjector;
import com.architecture.hexagonal.domain.model.vo.email.EmailBlockRulesVo;
import lombok.Builder;

@Builder
public class UserEmailProjectorTestDataBuilder {

  @Builder.Default private String host = "example";

  @Builder.Default private Boolean blockEmail = false;

  @Builder.Default
  private EmailBlockRulesVo blockedRules =
      EmailBlockRulesVoTestDataBuilder.builder().build().emailBlockRulesVo();

  public UserEmailProjector userEmailProjector() {
    return UserEmailProjector.builder()
        .host(host)
        .blockEmail(blockEmail)
        .blockedRules(blockedRules)
        .build();
  }
}
