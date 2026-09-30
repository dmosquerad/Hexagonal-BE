package com.architecture.hexagonal.application.testutils.usecase.business.user.getall.input;

import com.architecture.hexagonal.application.usecase.business.user.getall.input.GetUsersInput;
import com.architecture.hexagonal.domain.model.vo.pagination.Pagination;
import com.architecture.hexagonal.application.testutils.model.aggregate.pagination.PaginationTestDataBuilder;
import com.architecture.hexagonal.application.testutils.model.vo.email.EmailBlockRulesVoTestDataBuilder;
import com.architecture.hexagonal.domain.model.vo.email.EmailBlockRulesVo;
import lombok.Builder;

@Builder
public class GetUsersInputTestDataBuilder {

  @Builder.Default
  private String host = "example";

  @Builder.Default
  private Boolean blockEmail = null;

  @Builder.Default
  private Pagination pagination = PaginationTestDataBuilder.builder().build().pagination();

  @Builder.Default
  private EmailBlockRulesVo emailBlockRulesVo = EmailBlockRulesVoTestDataBuilder.builder().build().emailBlockRulesVo();

  public GetUsersInput getUsersInput() {
    return GetUsersInput.builder()
        .host(host)
        .blockEmail(blockEmail)
        .pagination(pagination)
        .blockedRules(emailBlockRulesVo)
        .build();
  }
}
