package com.architecture.hexagonal.infrastructure.inbound.messaging.kafka.mapper.converter;

import com.architecture.hexagonal.application.databuilder.model.vo.time.DateTestDataBuilder;
import com.architecture.hexagonal.application.model.vo.time.Date;
import com.architecture.hexagonal.domain.databuilder.model.vo.time.TestTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class DateConverterTest {

  @Test
  void toDateShouldConvertOffsetDateTime() {
    final OffsetDateTime offsetDateTime = TestTime.FIXED_INSTANT.atOffset(ZoneOffset.UTC);
    final Date expected = DateTestDataBuilder.builder().build().date();

    final Date result = DateConverter.toDate(offsetDateTime);

    Assertions.assertThat(result).usingRecursiveComparison().isEqualTo(expected);
  }
}
