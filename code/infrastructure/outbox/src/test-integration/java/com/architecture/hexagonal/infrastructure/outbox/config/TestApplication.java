package com.architecture.hexagonal.infrastructure.outbox.config;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Primary;

@EnableAutoConfiguration
@ComponentScan(
    basePackages = {
      "com.architecture.hexagonal.infrastructure.outbox"
    })
public class TestApplication {

    @Bean
    ApplicationEventPublisher applicationEventPublisher() {
        return event -> {};
    }
}
