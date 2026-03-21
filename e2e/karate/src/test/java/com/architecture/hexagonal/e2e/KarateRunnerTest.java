package com.architecture.hexagonal.e2e;

import com.intuit.karate.junit5.Karate;
import java.util.Locale;

class KarateRunnerTest {

  @Karate.Test
  Karate testAll() {
    final String mode = System.getProperty("executionMode", "all").toLowerCase(Locale.ROOT);

    return switch (mode) {
      case "smoke" -> Karate.run("classpath:features").tags("@smoke");
      case "features" -> Karate.run("classpath:features").tags("~@smoke");
      default -> Karate.run("classpath:features");
    };
  }
}
