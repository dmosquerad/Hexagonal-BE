package com.architecture.hexagonal.infrastructure.inbound.rest.config;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import java.time.Clock;

@EnableAutoConfiguration
@ComponentScan(basePackages = {
        "com.architecture.hexagonal.infrastructure.inbound.rest.config",
        "com.architecture.hexagonal.infrastructure.inbound.rest.exception",
        "com.architecture.hexagonal.infrastructure.inbound.rest.mapper"})
public class TestApplication {

    @Bean
    public Clock clock() {
        return Clock.systemUTC();
    }
}