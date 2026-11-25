package com.architecture.hexagonal.infrastructure.outbound.event.rabbitmq.config;

import java.time.Clock;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqClockConfig {

  public static final String RABBIT_MQ_CLOCK = "rabbitMqClock";

  @Bean(RABBIT_MQ_CLOCK)
  public Clock rabbitMqClock() {
    return Clock.systemUTC();
  }
}
