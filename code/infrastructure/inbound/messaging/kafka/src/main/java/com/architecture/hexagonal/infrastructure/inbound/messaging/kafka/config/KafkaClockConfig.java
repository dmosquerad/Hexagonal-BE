package com.architecture.hexagonal.infrastructure.inbound.messaging.kafka.config;

import java.time.Clock;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaClockConfig {

  public static final String KAFKA_CLOCK = "kafkaClock";

  @Bean(KAFKA_CLOCK)
  public Clock kafkaClock() {
    return Clock.systemUTC();
  }
}
