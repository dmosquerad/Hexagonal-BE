package com.architecture.hexagonal.domain.databuilder.model.criteria.user;

import com.architecture.hexagonal.domain.databuilder.model.vo.email.EmailBlockRulesVoTestDataBuilder;
import com.architecture.hexagonal.domain.model.criteria.user.UserEmailCriteria;
import com.architecture.hexagonal.domain.model.vo.email.EmailBlockRulesVo;
import lombok.Builder;

@Builder
public class UserEmailCriteriaTestDataBuilder {

  @Builder.Default private String host = "example";

  @Builder.Default private Boolean blockEmail = false;

  @Builder.Default
  private EmailBlockRulesVo blockedRules =
      EmailBlockRulesVoTestDataBuilder.builder().build().emailBlockRulesVo();

  public UserEmailCriteria userEmailCriteria() {
    return UserEmailCriteria.builder()
        .host(host)
        .blockEmail(blockEmail)
        .blockedRules(blockedRules)
        .build();
  }
}
