package com.architecture.hexagonal.infrastructure.inbound.rest.testutils.model.vo.email;

import com.architecture.hexagonal.domain.model.vo.email.EmailVo;
import lombok.Builder;

@Builder
public class EmailVoTestDataBuilder {

  @Builder.Default
  private String username = "test";

  @Builder.Default
  private String host = "example";

  @Builder.Default
  private String tld = "com";

  public EmailVo emailVo() {
    return EmailVo.builder()
        .username(username)
        .host(host)
        .tld(tld)
        .build();
  }
}