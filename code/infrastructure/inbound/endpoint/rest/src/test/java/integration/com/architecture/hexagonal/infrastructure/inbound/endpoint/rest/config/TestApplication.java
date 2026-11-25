package com.architecture.hexagonal.infrastructure.inbound.endpoint.rest.config;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@EnableAutoConfiguration
@ComponentScan(basePackages = {
        "com.architecture.hexagonal.infrastructure.inbound.endpoint.rest.config",
        "com.architecture.hexagonal.infrastructure.inbound.endpoint.rest.exception",
        "com.architecture.hexagonal.infrastructure.inbound.endpoint.rest.mapper"})
public class TestApplication {
}