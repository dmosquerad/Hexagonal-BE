package com.architecture.hexagonal.infrastructure.outbound.configuration.testutils.data.vo;

import com.architecture.hexagonal.domain.model.vo.EmailBlockRulesVo;
import java.util.Set;

import lombok.Builder;

@Builder
public class EmailBlockRulesVoTestDataBuilder {

  @Builder.Default
  private Set<String> email = Set.of("blocked@example.com");

  @Builder.Default
  private Set<String> host = Set.of("blocked");

  @Builder.Default
  private Set<String> tld = Set.of("xyz");

  @Builder.Default
  private Set<String> domain = Set.of("blocked.xyz");

  @Builder.Default
  private Set<String> username = Set.of("spammer");

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
