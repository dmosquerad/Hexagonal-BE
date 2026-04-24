package com.architecture.hexagonal.infrastructure.inbound.rest.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

@EnableAutoConfiguration
@ComponentScan(basePackages = {
        "com.architecture.hexagonal.infrastructure.inbound.rest.config",
        "com.architecture.hexagonal.infrastructure.inbound.rest.exception",
        "com.architecture.hexagonal.infrastructure.inbound.rest.mapper"})
public class TestApplication {

  @Bean
  public ObjectMapper objectMapper() {
    return new ObjectMapper().findAndRegisterModules();
  }

}