package com.architecture.hexagonal.infrastructure.outbound.database.postgresql.config;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Clock;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;
import org.hibernate.cfg.JdbcSettings;
import org.junit.jupiter.api.Test;

class PostgresConversionConfigTest {

  @Test
  void hibernateTimeZoneCustomizerShouldUseClockZone() {
    final ZoneId zoneId = ZoneId.of("Europe/Madrid");
    final Clock clock = Clock.system(zoneId);
    final Map<String, Object> hibernateProperties = new HashMap<>();

    new PostgresConversionConfig()
        .hibernateTimeZoneCustomizer(clock)
        .customize(hibernateProperties);

    assertThat(hibernateProperties)
        .containsEntry(JdbcSettings.JDBC_TIME_ZONE, TimeZone.getTimeZone(zoneId));
  }
}
