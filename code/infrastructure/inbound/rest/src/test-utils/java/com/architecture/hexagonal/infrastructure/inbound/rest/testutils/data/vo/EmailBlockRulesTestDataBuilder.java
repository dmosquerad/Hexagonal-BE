package com.architecture.hexagonal.infrastructure.inbound.rest.testutils.data.vo;

import com.architecture.hexagonal.domain.model.vo.EmailBlockRulesVo;
import java.util.Set;

import lombok.Builder;

@Builder
public class EmailBlockRulesTestDataBuilder {

  @Builder.Default
  private Set<String> email = Set.of("test@example.com");

  @Builder.Default
  private Set<String> host = Set.of("example");

  @Builder.Default
  private Set<String> tld = Set.of("com");

  @Builder.Default
  private Set<String> domain = Set.of("example.com");

  @Builder.Default
  private Set<String> username = Set.of("test");

  public EmailBlockRulesVo emailBlockRules() {
    return EmailBlockRulesVo.builder()
        .email(email)
        .host(host)
        .tld(tld)
        .domain(domain)
        .username(username)
        .build();
  }
}
