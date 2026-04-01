package com.architecture.hexagonal.infrastructure.outbound.configuration.config;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@EnableAutoConfiguration
@ConfigurationPropertiesScan(basePackages = {
    "com.architecture.hexagonal.infrastructure.outbound.configuration.config"
})
public class TestApplication {

}
