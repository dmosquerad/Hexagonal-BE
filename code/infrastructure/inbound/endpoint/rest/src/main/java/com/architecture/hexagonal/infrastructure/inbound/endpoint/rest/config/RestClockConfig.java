package com.architecture.hexagonal.infrastructure.inbound.endpoint.rest.config;

import java.time.Clock;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RestClockConfig {

  public static final String REST_CLOCK = "restClock";

  @Bean(REST_CLOCK)
  public Clock restClock() {
    return Clock.systemUTC();
  }
}
