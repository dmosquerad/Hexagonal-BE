package com.architecture.hexagonal.domain.databuilder.model.vo.time;

import com.architecture.hexagonal.domain.model.vo.time.DateVo;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;

import lombok.Builder;

@Builder
public class DateVoTestDataBuilder {

  @Builder.Default private Instant date = TestTime.FIXED_INSTANT;

  @Builder.Default private ZoneId zone = ZoneOffset.UTC;

  public DateVo dateVo() {
    return DateVo.builder().date(date).zone(zone).build();
  }
}
