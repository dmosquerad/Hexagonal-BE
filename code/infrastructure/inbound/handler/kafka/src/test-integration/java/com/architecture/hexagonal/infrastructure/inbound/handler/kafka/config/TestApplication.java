package com.architecture.hexagonal.infrastructure.inbound.handler.kafka.config;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.kafka.autoconfigure.KafkaAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@EnableAutoConfiguration(exclude = KafkaAutoConfiguration.class)
@ComponentScan(basePackages = {
    "com.architecture.hexagonal.infrastructure.inbound.handler.kafka.config",
    "com.architecture.hexagonal.infrastructure.inbound.handler.kafka.mapper"
})
public class TestApplication {}
