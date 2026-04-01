package com.architecture.hexagonal.infrastructure.outbound.configuration.config;

import jakarta.validation.constraints.Email;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@Data
@ConfigurationProperties(prefix = "email.block")
public class EmailBlockConfig {

    private List<@Email String> email;

    private List<String> host;

    private List<String> tld;

    private List<String> domain;

    private List<String> username;
}
