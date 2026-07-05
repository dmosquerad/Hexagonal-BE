package com.architecture.hexagonal.infrastructure.outbound.database.postgresql.config;

import java.time.Clock;
import java.util.TimeZone;
import org.hibernate.cfg.JdbcSettings;
import org.springframework.boot.hibernate.autoconfigure.HibernatePropertiesCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PostgresConversionConfig {

  @Bean
  public HibernatePropertiesCustomizer hibernateTimeZoneCustomizer(Clock clock) {
    return hibernateProperties ->
        hibernateProperties.put(JdbcSettings.JDBC_TIME_ZONE, TimeZone.getTimeZone(clock.getZone()));
  }
}
