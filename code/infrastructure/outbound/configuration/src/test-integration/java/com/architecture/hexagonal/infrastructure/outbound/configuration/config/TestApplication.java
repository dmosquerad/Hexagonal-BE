package com.architecture.hexagonal.infrastructure.outbound.configuration.config;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("configuration")
@TestConfiguration
@EnableAutoConfiguration
@ConfigurationPropertiesScan(basePackages = {
        "com.architecture.hexagonal.infrastructure.outbound.configuration.config"
})
@ComponentScan(basePackages = "com.architecture.hexagonal.infrastructure.outbound.configuration")
public class TestApplication {

}
