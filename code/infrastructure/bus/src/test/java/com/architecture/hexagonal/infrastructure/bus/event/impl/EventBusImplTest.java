package com.architecture.hexagonal.infrastructure.bus.event.impl;

import com.architecture.hexagonal.application.port.in.bus.event.EventHandler;
import com.architecture.hexagonal.infrastructure.bus.BusHandlerFinder;
import com.architecture.hexagonal.infrastructure.bus.middleware.impl.BusLoggingMiddleware;
import java.util.ArrayList;
import java.util.List;
import lombok.NonNull;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.support.StaticListableBeanFactory;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;

@ExtendWith(OutputCaptureExtension.class)
class EventBusImplTest {

  private record TestEvent(String value) {}

  private static final class RecordingHandler implements EventHandler<TestEvent> {
    private final List<TestEvent> received = new ArrayList<>();

    @Override
    public void handle(final @NonNull TestEvent event) {
      received.add(event);
    }
  }

  private static final class FailingHandler implements EventHandler<TestEvent> {
    private final String message;

    private FailingHandler(final String message) {
      this.message = message;
    }

    @Override
    public void handle(final @NonNull TestEvent event) {
      throw new IllegalStateException(message);
    }
  }

  @Test
  void publishShouldDeliverEventToAllHandlers() {
    final RecordingHandler first = new RecordingHandler();
    final RecordingHandler second = new RecordingHandler();
    final EventBusImpl eventBus =
        new EventBusImpl(finderWith(first, second), new BusLoggingMiddleware());
    final TestEvent event = new TestEvent("value");

    eventBus.publish(event);

    Assertions.assertThat(first.received).containsExactly(event);
    Assertions.assertThat(second.received).containsExactly(event);
  }

  @Test
  void publishShouldNotFailWhenNoHandlerIsRegistered() {
    final EventBusImpl eventBus = new EventBusImpl(finderWith(), new BusLoggingMiddleware());

    Assertions.assertThatCode(() -> eventBus.publish(new TestEvent("value")))
        .doesNotThrowAnyException();
  }

  @Test
  void publishShouldNotPropagateHandlerFailuresAndInvokeRemainingHandlers(
      final CapturedOutput output) {
    final RecordingHandler recording = new RecordingHandler();
    final EventBusImpl eventBus =
        new EventBusImpl(
            finderWith(new FailingHandler("sensitive"), recording, new FailingHandler("other")),
            new BusLoggingMiddleware());

    Assertions.assertThatCode(() -> eventBus.publish(new TestEvent("value")))
        .doesNotThrowAnyException();

    Assertions.assertThat(recording.received).hasSize(1);
    Assertions.assertThat(output)
        .contains("FailingHandler failed handling TestEvent (IllegalStateException)")
        .doesNotContain("sensitive");
  }

  private BusHandlerFinder finderWith(final Object... handlers) {
    final StaticListableBeanFactory beanFactory = new StaticListableBeanFactory();
    for (int index = 0; index < handlers.length; index++) {
      beanFactory.addBean("handler" + index, handlers[index]);
    }
    final BusHandlerFinder finder = new BusHandlerFinder(beanFactory);
    finder.afterSingletonsInstantiated();
    return finder;
  }
}
