package com.architecture.hexagonal.domain.model.vo.time.factory;

import com.architecture.hexagonal.domain.model.vo.time.DateVo;
import java.time.Clock;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import lombok.NonNull;
import lombok.experimental.UtilityClass;

@UtilityClass
public class DateVoFactory {

  public static DateVo now() {
    return from(Instant.now());
  }

  public static DateVo now(@NonNull final Clock clock) {
    return from(OffsetDateTime.now(clock));
  }

  public static DateVo from(@NonNull final Instant instant) {
    return DateVo.builder().date(instant).zone(ZoneOffset.UTC).build();
  }

  public static DateVo from(@NonNull final OffsetDateTime offsetDateTime) {
    return DateVo.builder()
        .date(offsetDateTime.toInstant())
        .zone(offsetDateTime.getOffset())
        .build();
  }
}
