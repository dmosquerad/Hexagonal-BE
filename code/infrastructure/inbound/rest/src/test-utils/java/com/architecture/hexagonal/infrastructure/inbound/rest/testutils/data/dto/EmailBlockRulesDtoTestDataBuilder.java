package com.architecture.hexagonal.infrastructure.inbound.rest.testutils.data.dto;

import com.architecture.hexagonal.infrastructure.inbound.contract.rest.email.dto.EmailBlockRulesDto;
import java.util.List;
import lombok.Builder;

@Builder
public class EmailBlockRulesDtoTestDataBuilder {

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

  public EmailBlockRulesDto emailBlockRulesDto() {
    EmailBlockRulesDto emailBlockRulesDto = new EmailBlockRulesDto();
    emailBlockRulesDto.setEmail(this.email);
    emailBlockRulesDto.setHost(this.host);
    emailBlockRulesDto.setTld(this.tld);
    emailBlockRulesDto.setDomain(this.domain);
    emailBlockRulesDto.setUsername(this.username);

    return emailBlockRulesDto;
  }
}
