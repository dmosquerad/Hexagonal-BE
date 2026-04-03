package com.architecture.hexagonal.infrastructure.inbound.rest.config;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@EnableAutoConfiguration
@ComponentScan(basePackages = {
        "com.architecture.hexagonal.infrastructure.inbound.rest.config",
        "com.architecture.hexagonal.infrastructure.inbound.rest.mapper"})
public class TestApplication {

}