package com.architecture.hexagonal.application.mapper.converter;

import java.time.ZoneId;
import java.util.Objects;
import lombok.experimental.UtilityClass;
import org.mapstruct.Named;

@UtilityClass
public class DateConverter {

  public static final String TO_ZONE_ID = "toZoneId";

  @Named(TO_ZONE_ID)
  public static ZoneId toZoneId(final String zoneId) {
    if (Objects.isNull(zoneId)) {
      return null;
    }

    return ZoneId.of(zoneId);
  }
}
