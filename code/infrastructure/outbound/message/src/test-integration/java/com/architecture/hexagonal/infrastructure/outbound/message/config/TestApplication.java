package com.architecture.hexagonal.infrastructure.outbound.message.config;

import org.springframework.batch.infrastructure.support.transaction.ResourcelessTransactionManager;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.PlatformTransactionManager;

@EnableAutoConfiguration
@ComponentScan(basePackages = {
    "com.architecture.hexagonal.infrastructure.outbound.message.config",
    "com.architecture.hexagonal.infrastructure.outbound.message.mapper",
    "com.architecture.hexagonal.infrastructure.outbound.message.adapter",
    "com.architecture.hexagonal.infrastructure.outbound.message.sender"
})
public class TestApplication {
    @Bean
    public PlatformTransactionManager transactionManager() {
        return new ResourcelessTransactionManager();
    }
}
