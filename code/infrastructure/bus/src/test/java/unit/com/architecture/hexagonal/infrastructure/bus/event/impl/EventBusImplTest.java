package com.architecture.hexagonal.infrastructure.bus.event.impl;

import com.architecture.hexagonal.domain.databuilder.model.event.user.UserCreatedEventTestDataBuilder;
import com.architecture.hexagonal.domain.model.event.user.UserCreatedEvent;
import com.architecture.hexagonal.domain.port.in.bus.middleware.BusMiddleware;
import com.architecture.hexagonal.domain.port.out.bus.event.EventPublisher;
import com.architecture.hexagonal.infrastructure.bus.BusHandlerFinder;
import com.architecture.hexagonal.infrastructure.bus.BusMessageValidator;
import com.architecture.hexagonal.infrastructure.bus.middleware.impl.BusLoggingMiddleware;
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

  @Mock private BusMessageValidator messageValidator;

  @Spy private BusMiddleware loggingMiddleware = new BusLoggingMiddleware();

  @InjectMocks private EventBusImpl eventBus;

  @Mock private EventPublisher<UserCreatedEvent> firstHandler;

  @Mock private EventPublisher<UserCreatedEvent> secondHandler;

  @Mock private EventPublisher<UserCreatedEvent> failingHandler;

  @Mock private EventPublisher<UserCreatedEvent> recordingHandler;

  @Mock private EventPublisher<UserCreatedEvent> secondFailingHandler;

  @Test
  void publishShouldDeliverEventToAllHandlers() {
    final UserCreatedEvent event =
        UserCreatedEventTestDataBuilder.builder().build().userCreatedEvent();
    Mockito.doReturn(List.of(firstHandler, secondHandler))
        .when(handlerFinder)
        .findAll(event, EventPublisher.class);

    eventBus.publish(event);

    Mockito.verify(messageValidator).validate(event);
    Mockito.verify(firstHandler).publish(event);
    Mockito.verify(secondHandler).publish(event);
  }

  @Test
  void publishShouldNotFailWhenNoHandlerIsRegistered() {
    final UserCreatedEvent event =
        UserCreatedEventTestDataBuilder.builder().build().userCreatedEvent();
    Mockito.doReturn(List.of()).when(handlerFinder).findAll(event, EventPublisher.class);

    Assertions.assertThatCode(() -> eventBus.publish(event)).doesNotThrowAnyException();
  }

  @Test
  void publishShouldNotPropagateHandlerFailuresAndInvokeRemainingHandlers(
      final CapturedOutput output) {
    final UserCreatedEvent event =
        UserCreatedEventTestDataBuilder.builder().build().userCreatedEvent();
    Mockito.doThrow(new IllegalStateException("sensitive")).when(failingHandler).publish(event);
    Mockito.doThrow(new IllegalStateException("other")).when(secondFailingHandler).publish(event);
    Mockito.doReturn(List.of(failingHandler, recordingHandler, secondFailingHandler))
        .when(handlerFinder)
        .findAll(event, EventPublisher.class);

    Assertions.assertThatCode(() -> eventBus.publish(event)).doesNotThrowAnyException();

    Mockito.verify(recordingHandler).publish(event);
    Assertions.assertThat(output)
        .contains("failed handling UserCreatedEvent (IllegalStateException)")
        .doesNotContain("sensitive");
  }
}
