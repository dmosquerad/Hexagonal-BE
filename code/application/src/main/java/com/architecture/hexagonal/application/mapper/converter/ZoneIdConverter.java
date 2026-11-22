package com.architecture.hexagonal.application.mapper.converter;

import java.time.ZoneId;
import lombok.experimental.UtilityClass;
import org.mapstruct.Named;

@UtilityClass
public class ZoneIdConverter {
  public static final String TO_ZONE_ID = "toZoneId";

  @Named(TO_ZONE_ID)
  public static ZoneId toZoneId(final String zone) {
    return ZoneId.of(zone);
  }
}
