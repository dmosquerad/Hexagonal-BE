package com.architecture.hexagonal.application.databuilder.model.common;

import com.architecture.hexagonal.contract.bus.model.common.Date;
import com.architecture.hexagonal.domain.databuilder.model.time.TestClock;
import java.time.Instant;
import lombok.Builder;

@Builder
public class DateTestDataBuilder {

  @Builder.Default private Instant instant = TestClock.FIXED_INSTANT;

  @Builder.Default private String zoneId = TestClock.FIXED_CLOCK.getZone().getId();

  public Date date() {
    final Date date = new Date();
    date.setDate(instant);
    date.setZoneId(zoneId);
    return date;
  }
}
