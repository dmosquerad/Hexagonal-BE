package com.architecture.hexagonal.infrastructure.outbound.database.mongodb.mapper.converter;

import java.time.OffsetDateTime;
import java.time.ZonedDateTime;
import java.util.Objects;
import lombok.experimental.UtilityClass;
import org.mapstruct.Named;

@UtilityClass
public class OffsetDateTimeConverter {

  public static final String TO_OFFSET_DATE_TIME = "toOffsetDateTime";

  @Named(TO_OFFSET_DATE_TIME)
  public static OffsetDateTime toOffsetDateTime(final ZonedDateTime zonedDateTime) {
    if (Objects.isNull(zonedDateTime)) {
      return null;
    }

    return zonedDateTime.toInstant().atOffset(zonedDateTime.getOffset());
  }
}
