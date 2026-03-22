package com.architecture.hexagonal.infrastructure.inbound.rest.testutils.data.vo;

import com.architecture.hexagonal.domain.data.vo.EmailVo;
import lombok.Builder;

@Builder
public class EmailVoTestDataBuilder {

  @Builder.Default
  private String username = "test";

  @Builder.Default
  private String domain = "example.com";

  public EmailVo emailVo() {
    return EmailVo.builder()
        .username(username)
        .domain(domain)
        .build();
  }
}