package com.architecture.hexagonal.application.testutils.user.getall.input;

import com.architecture.hexagonal.application.business.user.getall.input.GetUsersInput;
import com.architecture.hexagonal.domain.model.aggregate.pagination.Pagination;
import com.architecture.hexagonal.application.testutils.data.aggregate.pagination.PaginationTestDataBuilder;
import com.architecture.hexagonal.application.testutils.data.vo.EmailBlockRulesVoTestDataBuilder;
import com.architecture.hexagonal.domain.model.vo.EmailBlockRulesVo;
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
