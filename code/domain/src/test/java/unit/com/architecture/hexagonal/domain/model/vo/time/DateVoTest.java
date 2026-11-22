package com.architecture.hexagonal.domain.model.vo.time;

import com.architecture.hexagonal.domain.databuilder.model.vo.time.DateVoTestDataBuilder;
import java.time.Instant;
import java.time.ZoneId;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class DateVoTest {

  @Test
  void getOffsetDateTimeShouldUseZoneOffsetAtEachInstant() {
    final ZoneId zone = ZoneId.of("Europe/Madrid");
    final DateVo winter =
        DateVoTestDataBuilder.builder()
            .date(Instant.parse("2024-01-15T12:00:00Z"))
            .zone(zone)
            .build()
            .dateVo();
    final DateVo summer =
        DateVoTestDataBuilder.builder()
            .date(Instant.parse("2024-07-15T12:00:00Z"))
            .zone(zone)
            .build()
            .dateVo();

    Assertions.assertThat(winter.getOffsetDateTime().toString())
        .isEqualTo("2024-01-15T13:00+01:00");
    Assertions.assertThat(summer.getOffsetDateTime().toString())
        .isEqualTo("2024-07-15T14:00+02:00");
  }
}
