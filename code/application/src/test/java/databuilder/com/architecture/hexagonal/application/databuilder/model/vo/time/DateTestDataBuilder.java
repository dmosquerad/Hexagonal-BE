package com.architecture.hexagonal.application.databuilder.model.vo.time;

import com.architecture.hexagonal.application.model.vo.time.Date;
import java.time.Instant;
import java.time.ZoneOffset;

import lombok.Builder;

@Builder
public class DateTestDataBuilder {

  @Builder.Default private Instant date = Instant.parse("2026-01-01T00:00:00Z");

  @Builder.Default private String zone = ZoneOffset.UTC.getId();

  public Date date() {
    return Date.builder().date(date).zone(zone).build();
  }
}
