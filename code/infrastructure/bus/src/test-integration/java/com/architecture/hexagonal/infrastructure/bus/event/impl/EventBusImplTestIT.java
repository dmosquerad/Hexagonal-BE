package com.architecture.hexagonal.infrastructure.bus.event.impl;

import com.architecture.hexagonal.application.port.out.bus.event.EventBus;
import com.architecture.hexagonal.contract.bus.model.user.event.UserCreatedEvent;
import com.architecture.hexagonal.infrastructure.bus.BusHandlerFinder;
import com.architecture.hexagonal.infrastructure.bus.config.TestApplication;
import com.architecture.hexagonal.infrastructure.bus.middleware.impl.BusLoggingMiddleware;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest(
    classes = {EventBusImpl.class, BusHandlerFinder.class, BusLoggingMiddleware.class})
@ContextConfiguration(classes = TestApplication.class)
class EventBusImplTestIT {

  @Autowired private EventBus eventBus;

  @Test
  void publishShouldCompleteWhenNoHandlerIsRegistered() {
    final UserCreatedEvent event = new UserCreatedEvent();

    AssertionsForClassTypes.assertThatCode(() -> eventBus.publish(event))
        .doesNotThrowAnyException();
  }
}
