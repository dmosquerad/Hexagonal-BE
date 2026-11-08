package com.architecture.hexagonal.infrastructure.outbound.database.mongodb.config;

import java.time.Clock;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.core.convert.support.GenericConversionService;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;

class MongodbConfigTest {

  private final MongodbConfig config = new MongodbConfig();

  @Test
  void mongoCustomConversionsShouldConvertZonedDateTimeToDateAndBackUsingClockZone() {
    final ZoneId zoneId = ZoneId.of("Europe/Madrid");
    final Clock clock = Clock.system(zoneId);
    final MongoCustomConversions conversions = config.mongoCustomConversions(clock);
    final GenericConversionService conversionService = new GenericConversionService();
    conversions.registerConvertersIn(conversionService);
    final ZonedDateTime zonedDateTime =
        ZonedDateTime.parse("2025-03-30T03:30:00+02:00[Europe/Paris]");
    final Date expectedDate = Date.from(zonedDateTime.toInstant());

    final Date convertedDate = conversionService.convert(zonedDateTime, Date.class);
    final ZonedDateTime convertedZonedDateTime =
        conversionService.convert(expectedDate, ZonedDateTime.class);

    Assertions.assertThat(convertedDate).isEqualTo(expectedDate);
    Assertions.assertThat(convertedZonedDateTime)
        .isEqualTo(expectedDate.toInstant().atZone(zoneId));
  }

  @Test
  void mongoCustomConversionsShouldConvertOffsetDateTimeToDateAndBackUsingClockZone() {
    final ZoneId zoneId = ZoneId.of("America/Los_Angeles");
    final Clock clock = Clock.system(zoneId);
    final MongoCustomConversions conversions = config.mongoCustomConversions(clock);
    final GenericConversionService conversionService = new GenericConversionService();
    conversions.registerConvertersIn(conversionService);
    final OffsetDateTime offsetDateTime = OffsetDateTime.parse("2025-01-15T10:30:00+03:00");
    final Date expectedDate = Date.from(offsetDateTime.toInstant());

    final Date convertedDate = conversionService.convert(offsetDateTime, Date.class);
    final OffsetDateTime convertedOffsetDateTime =
        conversionService.convert(expectedDate, OffsetDateTime.class);

    Assertions.assertThat(convertedDate).isEqualTo(expectedDate);
    Assertions.assertThat(convertedOffsetDateTime)
        .isEqualTo(expectedDate.toInstant().atZone(zoneId).toOffsetDateTime());
  }
}
