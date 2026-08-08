package com.architecture.hexagonal.infrastructure.outbound.message.outbox.config;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@EnableAutoConfiguration
@ComponentScan(
    basePackages = {
      "com.architecture.hexagonal.infrastructure.outbound.message.outbox.config",
      "com.architecture.hexagonal.infrastructure.outbound.message.outbox.mapper",
      "com.architecture.hexagonal.infrastructure.outbound.message.outbox.adapter",
      "com.architecture.hexagonal.infrastructure.outbound.message.outbox.service"
    })
public class TestApplication {}
