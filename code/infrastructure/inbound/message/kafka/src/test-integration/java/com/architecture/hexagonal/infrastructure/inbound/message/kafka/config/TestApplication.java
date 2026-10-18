package com.architecture.hexagonal.infrastructure.inbound.message.kafka.config;

import java.time.Clock;
import org.springframework.context.annotation.Bean;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.kafka.autoconfigure.KafkaAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@EnableAutoConfiguration(exclude = KafkaAutoConfiguration.class)
@ComponentScan(basePackages = {
    "com.architecture.hexagonal.infrastructure.inbound.handler.kafka.config",
    "com.architecture.hexagonal.infrastructure.inbound.handler.kafka.mapper"
})
public class TestApplication {

  @Bean
  Clock clock() {
    return Clock.systemUTC();
  }
}
