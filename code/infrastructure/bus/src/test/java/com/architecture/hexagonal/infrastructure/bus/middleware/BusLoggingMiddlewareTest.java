package com.architecture.hexagonal.infrastructure.bus.middleware;

import com.architecture.hexagonal.infrastructure.bus.middleware.impl.BusLoggingMiddleware;
import java.time.Clock;
import java.time.Instant;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;

@ExtendWith({OutputCaptureExtension.class, MockitoExtension.class})
class BusLoggingMiddlewareTest {

  @Mock private Clock clock;

  @InjectMocks private BusLoggingMiddleware loggingMiddleware;

  @Test
  void shouldLogCompletionWithoutLoggingMessageContents(final CapturedOutput output) {
    Mockito.when(clock.instant()).thenReturn(Instant.EPOCH, Instant.EPOCH.plusMillis(42));

    final String result = loggingMiddleware.execute("sensitive", () -> "result");

    Assertions.assertThat(result).isEqualTo("result");
    Assertions.assertThat(output).contains("String completed in 42 ms").doesNotContain("sensitive");
  }

  @Test
  void shouldLogFailureAndRethrowWithoutLoggingMessageContents(final CapturedOutput output) {
    Mockito.when(clock.instant()).thenReturn(Instant.EPOCH, Instant.EPOCH.plusMillis(42));

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
