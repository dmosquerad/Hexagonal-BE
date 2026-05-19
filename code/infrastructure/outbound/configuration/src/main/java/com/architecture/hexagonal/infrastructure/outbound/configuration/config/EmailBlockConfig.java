package com.architecture.hexagonal.infrastructure.outbound.configuration.config;

import jakarta.validation.constraints.Email;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Set;

@Data
@ConfigurationProperties(prefix = "email.block")
public class EmailBlockConfig {

    private Set<@Email String> email;

    private Set<String> host;

    private Set<String> tld;

    private Set<String> domain;

    private Set<String> username;
}
