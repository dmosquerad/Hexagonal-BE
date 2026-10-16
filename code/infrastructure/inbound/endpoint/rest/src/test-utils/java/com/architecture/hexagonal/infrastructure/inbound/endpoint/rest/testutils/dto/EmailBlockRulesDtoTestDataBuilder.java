package com.architecture.hexagonal.infrastructure.inbound.endpoint.rest.testutils.dto;

import com.architecture.hexagonal.contract.inbound.endpoint.rest.email.server.dto.EmailBlockRulesDto;
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
    final EmailBlockRulesDto emailBlockRulesDto = new EmailBlockRulesDto();
    emailBlockRulesDto.setEmail(email);
    emailBlockRulesDto.setHost(host);
    emailBlockRulesDto.setTld(tld);
    emailBlockRulesDto.setDomain(domain);
    emailBlockRulesDto.setUsername(username);

    return emailBlockRulesDto;
  }
}
