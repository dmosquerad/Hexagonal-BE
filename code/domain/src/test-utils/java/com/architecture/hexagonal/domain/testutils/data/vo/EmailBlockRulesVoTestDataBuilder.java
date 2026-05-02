package com.architecture.hexagonal.domain.testutils.data.vo;

import com.architecture.hexagonal.domain.model.vo.EmailBlockRulesVo;
import java.util.List;
import lombok.Builder;

@Builder
public class EmailBlockRulesVoTestDataBuilder {

  @Builder.Default
  private List<String> email = List.of();

  @Builder.Default
  private List<String> host = List.of();

  @Builder.Default
  private List<String> tld = List.of();

  @Builder.Default
  private List<String> domain = List.of();

  @Builder.Default
  private List<String> username = List.of();

  public EmailBlockRulesVo emailBlockRulesVo() {
    return EmailBlockRulesVo.builder()
        .email(email)
        .host(host)
        .tld(tld)
        .domain(domain)
        .username(username)
        .build();
  }
}
