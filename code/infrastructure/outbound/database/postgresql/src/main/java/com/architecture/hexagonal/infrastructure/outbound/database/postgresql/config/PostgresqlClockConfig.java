package com.architecture.hexagonal.infrastructure.outbound.database.postgresql.config;

import java.time.Clock;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PostgresqlClockConfig {

  public static final String POSTGRESQL_CLOCK = "postgresqlClock";

  @Bean(POSTGRESQL_CLOCK)
  public Clock postgresqlClock() {
    return Clock.systemUTC();
  }
}
