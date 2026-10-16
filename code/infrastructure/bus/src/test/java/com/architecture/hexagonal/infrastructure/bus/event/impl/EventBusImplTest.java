package com.architecture.hexagonal.infrastructure.bus.event.impl;

import com.architecture.hexagonal.application.port.in.bus.event.EventHandler;
import com.architecture.hexagonal.contract.bus.model.user.event.UserCreatedEvent;
import com.architecture.hexagonal.infrastructure.bus.BusHandlerFinder;
import com.architecture.hexagonal.infrastructure.bus.middleware.BusMiddleware;
import com.architecture.hexagonal.infrastructure.bus.middleware.impl.BusLoggingMiddleware;
import java.time.Clock;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;

@ExtendWith({OutputCaptureExtension.class, MockitoExtension.class})
class EventBusImplTest {

  @Mock private BusHandlerFinder handlerFinder;

  @Spy private BusMiddleware loggingMiddleware = new BusLoggingMiddleware(Clock.systemUTC());

  @InjectMocks private EventBusImpl eventBus;

  @Mock private EventHandler<UserCreatedEvent> firstHandler;

  @Mock private EventHandler<UserCreatedEvent> secondHandler;

  @Mock private EventHandler<UserCreatedEvent> failingHandler;

  @Mock private EventHandler<UserCreatedEvent> recordingHandler;

  @Mock private EventHandler<UserCreatedEvent> secondFailingHandler;

  @Test
  void publishShouldDeliverEventToAllHandlers() {
    final UserCreatedEvent event = new UserCreatedEvent();
    Mockito.doReturn(List.of(firstHandler, secondHandler))
        .when(handlerFinder)
        .findAll(event, EventHandler.class);

    eventBus.publish(event);

    Mockito.verify(firstHandler).handle(event);
    Mockito.verify(secondHandler).handle(event);
  }

  @Test
  void publishShouldNotFailWhenNoHandlerIsRegistered() {
    final UserCreatedEvent event = new UserCreatedEvent();
    Mockito.doReturn(List.of()).when(handlerFinder).findAll(event, EventHandler.class);

    Assertions.assertThatCode(() -> eventBus.publish(event)).doesNotThrowAnyException();
  }

  @Test
  void publishShouldNotPropagateHandlerFailuresAndInvokeRemainingHandlers(
      final CapturedOutput output) {
    final UserCreatedEvent event = new UserCreatedEvent();
    Mockito.doThrow(new IllegalStateException("sensitive")).when(failingHandler).handle(event);
    Mockito.doThrow(new IllegalStateException("other")).when(secondFailingHandler).handle(event);
    Mockito.doReturn(List.of(failingHandler, recordingHandler, secondFailingHandler))
        .when(handlerFinder)
        .findAll(event, EventHandler.class);

    Assertions.assertThatCode(() -> eventBus.publish(event)).doesNotThrowAnyException();

    Mockito.verify(recordingHandler).handle(event);
    Assertions.assertThat(output)
        .contains("failed handling UserCreatedEvent (IllegalStateException)")
        .doesNotContain("sensitive");
  }
}
