package com.architecture.hexagonal.domain.testutils.data.vo;

import com.architecture.hexagonal.domain.data.vo.EmailVo;
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