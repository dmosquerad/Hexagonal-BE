package com.architecture.hexagonal.domain.model.vo.time.factory;

import com.architecture.hexagonal.domain.databuilder.model.vo.time.DateVoTestDataBuilder;
import com.architecture.hexagonal.domain.model.vo.time.DateVo;
import java.time.Clock;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class DateVoFactoryTest {

  @Test
  void nowShouldUseProvidedClock() {
    final Instant instant = Instant.parse("2024-07-15T12:30:00Z");
    final Clock clock = Clock.fixed(instant, ZoneOffset.ofHours(2));

    final DateVo result = DateVoFactory.now(clock);
    final DateVo expected =
        DateVoTestDataBuilder.builder().date(instant).zone(ZoneOffset.ofHours(2)).build().dateVo();

    Assertions.assertThat(result).isEqualTo(expected);
  }

  @Test
  void fromShouldUseUtcForInstant() {
    final Instant instant = Instant.parse("2024-07-15T12:30:00Z");

    final DateVo result = DateVoFactory.from(instant);
    final DateVo expected = DateVoTestDataBuilder.builder().date(instant).build().dateVo();

    Assertions.assertThat(result).isEqualTo(expected);
  }

  @Test
  void fromShouldPreserveOffsetDateTime() {
    final OffsetDateTime offsetDateTime = OffsetDateTime.parse("2024-07-15T14:30:00+02:00");

    final DateVo result = DateVoFactory.from(offsetDateTime);
    final DateVo expected =
        DateVoTestDataBuilder.builder()
            .date(Instant.parse("2024-07-15T12:30:00Z"))
            .zone(ZoneOffset.ofHours(2))
            .build()
            .dateVo();

    Assertions.assertThat(result).isEqualTo(expected);
  }
}
