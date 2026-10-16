package com.architecture.hexagonal.infrastructure.outbound.event.rabbitmq.config;

import org.springframework.batch.infrastructure.support.transaction.ResourcelessTransactionManager;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.PlatformTransactionManager;

import java.time.Clock;

@EnableAutoConfiguration
@ComponentScan(basePackages = {
    "com.architecture.hexagonal.infrastructure.outbound.event.rabbitmq.config",
    "com.architecture.hexagonal.infrastructure.outbound.event.rabbitmq.mapper",
    "com.architecture.hexagonal.infrastructure.outbound.event.rabbitmq.event",
    "com.architecture.hexagonal.infrastructure.outbound.event.rabbitmq.producer",
    "com.architecture.hexagonal.contract.outbound.event.rabbitmq"
})
public class TestApplication {
    @Bean
    public PlatformTransactionManager transactionManager() {
        return new ResourcelessTransactionManager();
    }

    @Bean
    public Clock clock() {
        return Clock.systemUTC();
    }
}
