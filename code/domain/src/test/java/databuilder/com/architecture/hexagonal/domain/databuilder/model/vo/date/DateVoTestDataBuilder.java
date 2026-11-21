package com.architecture.hexagonal.domain.databuilder.model.vo.date;

import com.architecture.hexagonal.domain.databuilder.model.time.TestClock;
import com.architecture.hexagonal.domain.model.vo.date.DateVo;
import java.time.Instant;
import java.time.ZoneId;
import lombok.Builder;

@Builder
public class DateVoTestDataBuilder {

  @Builder.Default private Instant date = TestClock.FIXED_INSTANT;

  @Builder.Default private ZoneId zoneId = TestClock.FIXED_CLOCK.getZone();

  public DateVo dateVo() {
    return DateVo.builder().date(date).zoneId(zoneId).build();
  }
}
