package com.architecture.hexagonal.application.testutils.data.vo;

import com.architecture.hexagonal.domain.model.vo.EmailBlockRulesVo;
import java.util.List;
import lombok.Builder;

@Builder
public class EmailBlockRulesVoTestDataBuilder {

  @Builder.Default
  private List<String> email = List.of("test@example.com");

  @Builder.Default
  private List<String> host = List.of("example");

  @Builder.Default
  private List<String> tld = List.of("com");

  @Builder.Default
  private List<String> domain = List.of("example.com");

  @Builder.Default
  private List<String> username = List.of("test");

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
