package com.architecture.hexagonal.infrastructure.bus.middleware;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;

@ExtendWith(OutputCaptureExtension.class)
class BusLoggingMiddlewareTest {

  private final BusLoggingMiddleware loggingMiddleware = new BusLoggingMiddleware();

  @Test
  void shouldLogCompletionWithoutLoggingMessageContents(final CapturedOutput output) {
    final String result = loggingMiddleware.execute(new TestMessage("sensitive"), () -> "result");

    assertThat(result).isEqualTo("result");
    assertThat(output).contains("TestMessage completed in").doesNotContain("sensitive");
  }

  @Test
  void shouldLogFailureAndRethrowWithoutLoggingMessageContents(final CapturedOutput output) {
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
        .contains("TestMessage failed in")
        .contains("IllegalArgumentException")
        .doesNotContain("sensitive");
  }

  private record TestMessage(String value) {}
}
