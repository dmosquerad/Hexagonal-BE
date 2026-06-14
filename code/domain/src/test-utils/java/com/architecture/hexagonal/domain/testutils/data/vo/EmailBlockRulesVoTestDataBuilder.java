package com.architecture.hexagonal.domain.testutils.data.vo;

import com.architecture.hexagonal.domain.model.vo.EmailBlockRulesVo;
import java.util.Set;

import lombok.Builder;

@Builder
public class EmailBlockRulesVoTestDataBuilder {

  @Builder.Default
  private Set<String> email = Set.of();

  @Builder.Default
  private Set<String> host = Set.of();

  @Builder.Default
  private Set<String> tld = Set.of();

  @Builder.Default
  private Set<String> domain = Set.of();

  @Builder.Default
  private Set<String> username = Set.of();

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
