package com.architecture.hexagonal.infrastructure.inbound.orchestration.config;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@EnableAutoConfiguration
@ComponentScan(basePackages = {
        "com.architecture.hexagonal.infrastructure.inbound.orchestration.config",
        "com.architecture.hexagonal.infrastructure.inbound.orchestration.mapper"
})
public class TestApplication {
}
