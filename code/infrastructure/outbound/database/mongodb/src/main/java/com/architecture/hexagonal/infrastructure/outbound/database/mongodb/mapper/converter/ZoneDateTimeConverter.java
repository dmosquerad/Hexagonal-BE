package com.architecture.hexagonal.infrastructure.outbound.database.mongodb.mapper.converter;

import java.time.OffsetDateTime;
import java.time.ZonedDateTime;
import java.util.Objects;
import lombok.experimental.UtilityClass;
import org.mapstruct.Named;

@UtilityClass
public class ZoneDateTimeConverter {

  public static final String TO_ZONED_DATE_TIME = "toZonedDateTime";

  @Named(TO_ZONED_DATE_TIME)
  public static ZonedDateTime toZonedDateTime(final OffsetDateTime offsetDateTime) {
    if (Objects.isNull(offsetDateTime)) {
      return null;
    }

    return offsetDateTime.toZonedDateTime();
  }
}
