package com.architecture.hexagonal.infrastructure.inbound.cqrs.config;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@EnableAutoConfiguration
@ComponentScan(basePackages = {
        "com.architecture.hexagonal.infrastructure.inbound.cqrs.config",
        "com.architecture.hexagonal.infrastructure.inbound.cqrs.mapper"
})
public class TestApplication {
}
