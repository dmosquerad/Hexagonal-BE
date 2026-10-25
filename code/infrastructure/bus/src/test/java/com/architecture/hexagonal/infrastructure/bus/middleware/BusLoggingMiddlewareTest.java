package com.architecture.hexagonal.infrastructure.bus.middleware;

import com.architecture.hexagonal.infrastructure.bus.middleware.impl.BusLoggingMiddleware;
import java.time.Clock;
import java.time.Instant;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;

@ExtendWith(OutputCaptureExtension.class)
class BusLoggingMiddlewareTest {

  @Test
  void shouldLogCompletionWithoutLoggingMessageContents(final CapturedOutput output) {
    final BusLoggingMiddleware loggingMiddleware = loggingMiddlewareWithElapsedTime(42);
    final String result = loggingMiddleware.execute(new TestMessage("sensitive"), () -> "result");

    Assertions.assertThat(result).isEqualTo("result");
  }

  @Test
  void shouldLogFailureAndRethrowWithoutLoggingMessageContents(final CapturedOutput output) {
    final BusLoggingMiddleware loggingMiddleware = loggingMiddlewareWithElapsedTime(42);
    final IllegalArgumentException exception = new IllegalArgumentException("failure");

    Assertions.assertThatThrownBy(
            () ->
                loggingMiddleware.execute(
                    new TestMessage("sensitive"),
                    () -> {
                      throw exception;
                    }))
        .isSameAs(exception);

    Assertions.assertThat(output)
        .contains("TestMessage failed in 42 ms")
        .contains("IllegalArgumentException")
        .doesNotContain("sensitive");
  }

  private BusLoggingMiddleware loggingMiddlewareWithElapsedTime(final long elapsedMillis) {
    final Clock clock = Mockito.mock(Clock.class);
    Mockito.when(clock.instant())
        .thenReturn(Instant.EPOCH, Instant.EPOCH.plusMillis(elapsedMillis));
    return new BusLoggingMiddleware(clock);
  }

  private record TestMessage(String value) {}
}
