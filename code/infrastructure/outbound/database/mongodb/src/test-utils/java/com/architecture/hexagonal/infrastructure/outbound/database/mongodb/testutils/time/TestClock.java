package com.architecture.hexagonal.infrastructure.outbound.database.mongodb.testutils.time;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;

public class TestClock {

  public static final Instant FIXED_INSTANT = Instant.parse("2026-01-01T00:00:00Z");
  public static final Clock FIXED_CLOCK = Clock.fixed(FIXED_INSTANT, ZoneOffset.UTC);
}
