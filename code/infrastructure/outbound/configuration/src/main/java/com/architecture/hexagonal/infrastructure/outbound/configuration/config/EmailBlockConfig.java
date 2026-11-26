package com.architecture.hexagonal.infrastructure.outbound.configuration.config;

import jakarta.validation.constraints.Email;
import java.util.HashSet;
import java.util.Set;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "email.block")
public class EmailBlockConfig {

  private final Set<@Email String> email = new HashSet<>();

  private final Set<String> host = new HashSet<>();

  private final Set<String> tld = new HashSet<>();

  private final Set<String> domain = new HashSet<>();

  private final Set<String> username = new HashSet<>();
}
