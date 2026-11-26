package com.architecture.hexagonal.infrastructure.bus.event.impl;

import com.architecture.hexagonal.domain.port.out.bus.event.EventBus;
import com.architecture.hexagonal.domain.databuilder.model.event.user.UserCreatedEventTestDataBuilder;
import com.architecture.hexagonal.domain.model.event.user.UserCreatedEvent;
import com.architecture.hexagonal.infrastructure.bus.BusHandlerFinder;
import com.architecture.hexagonal.infrastructure.bus.BusMessageValidator;
import com.architecture.hexagonal.infrastructure.bus.config.TestApplication;
import com.architecture.hexagonal.infrastructure.bus.middleware.impl.BusLoggingMiddleware;
import java.util.UUID;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest(
    classes = {
      EventBusImpl.class,
      BusHandlerFinder.class,
      BusMessageValidator.class,
      BusLoggingMiddleware.class
    })
@ContextConfiguration(classes = TestApplication.class)
class EventBusImplTestIT {

  @Autowired private EventBus eventBus;

  @Test
  void publishShouldCompleteWhenNoHandlerIsRegistered() {
    final UserCreatedEvent event =
        UserCreatedEventTestDataBuilder.builder()
            .userId(UUID.randomUUID())
            .build()
            .userCreatedEvent();

    AssertionsForClassTypes.assertThatCode(() -> eventBus.publish(event))
        .doesNotThrowAnyException();
  }

  @Test
  void publishShouldAcceptEventsWithoutContractConstraints() {
    final UserCreatedEvent event = UserCreatedEvent.builder().build();

    AssertionsForClassTypes.assertThatCode(() -> eventBus.publish(event))
        .doesNotThrowAnyException();
  }
}
