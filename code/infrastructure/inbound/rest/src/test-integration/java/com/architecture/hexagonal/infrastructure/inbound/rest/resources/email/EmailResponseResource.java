package com.architecture.hexagonal.infrastructure.inbound.rest.resources.email;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.core.io.Resource;

@TestConfiguration
public class EmailResponseResource {

  @Value("classpath:email/response/getBlockedRules.json")
  public Resource getBlockedRules;

}
