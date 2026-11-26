package com.architecture.hexagonal.application.mapper.converter;

import com.architecture.hexagonal.application.databuilder.model.vo.time.DateTestDataBuilder;
import com.architecture.hexagonal.domain.databuilder.model.vo.time.DateVoTestDataBuilder;
import com.architecture.hexagonal.domain.model.vo.time.DateVo;
import java.time.ZoneId;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class ZoneIdConverterTest {
  @Test
  void toZoneIdShouldConvertZoneIdentifier() {
    final ZoneId zone = ZoneId.of("Europe/Madrid");
    final String zoneId = DateTestDataBuilder.builder().zone(zone.getId()).build().date().zone();
    final DateVo expected = DateVoTestDataBuilder.builder().zone(zone).build().dateVo();

    final ZoneId result = ZoneIdConverter.toZoneId(zoneId);

    Assertions.assertThat(result).isEqualTo(expected.zone());
  }
}
