package com.architecture.hexagonal.application.testutils.time;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;

public class TestClock {
  public static final Instant FIXED_INSTANT = Instant.parse("2025-01-01T10:00:00Z");

  public static final Clock FIXED_CLOCK = Clock.fixed(FIXED_INSTANT, ZoneOffset.UTC);

  public static final ZoneId FIXED_ZONE = FIXED_CLOCK.getZone();
}
