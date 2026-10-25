package com.architecture.hexagonal.infrastructure.bus.middleware;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.architecture.hexagonal.infrastructure.bus.middleware.impl.BusLoggingMiddleware;
import java.time.Clock;
import java.time.Instant;
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

    assertThat(result).isEqualTo("result");
    assertThat(output).contains("TestMessage completed in 42 ms").doesNotContain("sensitive");
  }

  @Test
  void shouldLogFailureAndRethrowWithoutLoggingMessageContents(final CapturedOutput output) {
    final BusLoggingMiddleware loggingMiddleware = loggingMiddlewareWithElapsedTime(42);
    final IllegalArgumentException exception = new IllegalArgumentException("failure");

    assertThatThrownBy(
            () ->
                loggingMiddleware.execute(
                    new TestMessage("sensitive"),
                    () -> {
                      throw exception;
                    }))
        .isSameAs(exception);

    assertThat(output)
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
