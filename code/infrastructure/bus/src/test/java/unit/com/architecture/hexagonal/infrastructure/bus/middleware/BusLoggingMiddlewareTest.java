package com.architecture.hexagonal.infrastructure.bus.middleware;

import com.architecture.hexagonal.infrastructure.bus.middleware.impl.BusLoggingMiddleware;
import java.time.Instant;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;

@ExtendWith(OutputCaptureExtension.class)
class BusLoggingMiddlewareTest {

  private final BusLoggingMiddleware loggingMiddleware = new BusLoggingMiddleware();
  private MockedStatic<Instant> instantMock;

  @BeforeEach
  void setUp() {
    instantMock = Mockito.mockStatic(Instant.class, Mockito.CALLS_REAL_METHODS);
  }

  @AfterEach
  void tearDown() {
    instantMock.close();
  }

  @Test
  void shouldLogCompletionWithoutLoggingMessageContents(final CapturedOutput output) {
    final Instant endTime = Instant.EPOCH.plusMillis(42);
    instantMock.when(Instant::now).thenReturn(Instant.EPOCH, endTime);

    final String result = loggingMiddleware.execute("sensitive", () -> "result");

    Assertions.assertThat(result).isEqualTo("result");
    Assertions.assertThat(output).contains("String completed in 42 ms").doesNotContain("sensitive");
  }

  @Test
  void shouldLogFailureAndRethrowWithoutLoggingMessageContents(final CapturedOutput output) {
    final Instant endTime = Instant.EPOCH.plusMillis(42);
    instantMock.when(Instant::now).thenReturn(Instant.EPOCH, endTime);

    final IllegalArgumentException exception = new IllegalArgumentException("failure");

    Assertions.assertThatThrownBy(
            () ->
                loggingMiddleware.execute(
                    "sensitive",
                    () -> {
                      throw exception;
                    }))
        .isSameAs(exception);

    Assertions.assertThat(output)
        .contains("String failed in 42 ms")
        .contains("IllegalArgumentException")
        .doesNotContain("sensitive");
  }
}
