package com.architecture.hexagonal.infrastructure.outbound.configuration.config;

import jakarta.validation.constraints.Email;
import java.util.Set;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "email.block")
public class EmailBlockConfig {

  private Set<@Email String> email = Set.of();

  private Set<String> host = Set.of();

  private Set<String> tld = Set.of();

  private Set<String> domain = Set.of();

  private Set<String> username = Set.of();
}
