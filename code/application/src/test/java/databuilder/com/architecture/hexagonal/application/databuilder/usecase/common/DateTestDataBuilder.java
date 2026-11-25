package com.architecture.hexagonal.application.databuilder.usecase.common;

import com.architecture.hexagonal.contract.bus.model.common.Date;
import java.time.Instant;
import java.time.ZoneOffset;

import lombok.Builder;

@Builder
public class DateTestDataBuilder {

  @Builder.Default private Instant date = Instant.parse("2026-01-01T00:00:00Z");

  @Builder.Default private String zone = ZoneOffset.UTC.getId();

  public Date date() {
    final Date dateObject = new Date();
    dateObject.setDate(date);
    dateObject.setZone(zone);
    return dateObject;
  }
}
