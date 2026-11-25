package com.architecture.hexagonal.domain.model.vo.time;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import lombok.Builder;

@Builder
public record DateVo(Instant date, ZoneId zone) {
  public OffsetDateTime getOffsetDateTime() {
    return this.date.atZone(this.zone).toOffsetDateTime();
  }
}
