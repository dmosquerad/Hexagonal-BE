package com.architecture.hexagonal.infrastructure.outbound.event.rabbitmq.event;

import com.architecture.hexagonal.application.testutils.time.TestClock;
import com.architecture.hexagonal.contract.bus.model.user.event.UserCreatedEvent;
import com.architecture.hexagonal.contract.bus.model.user.event.UserDeletedEvent;
import com.architecture.hexagonal.contract.bus.model.user.event.UserUpdatedEvent;
import com.architecture.hexagonal.contract.outbound.event.rabbitmq.model.MessageHeader;
import com.architecture.hexagonal.contract.outbound.event.rabbitmq.model.UserCreated;
import com.architecture.hexagonal.contract.outbound.event.rabbitmq.model.UserData;
import com.architecture.hexagonal.contract.outbound.event.rabbitmq.model.UserDeleted;
import com.architecture.hexagonal.contract.outbound.event.rabbitmq.model.UserUpdated;
import com.architecture.hexagonal.infrastructure.outbound.event.rabbitmq.mapper.user.UserEventMessageMapper;
import java.time.OffsetDateTime;
import java.util.UUID;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.context.ApplicationEventPublisher;

class UserEventHandlersTest {

  private final ApplicationEventPublisher publisher = Mockito.mock(ApplicationEventPublisher.class);
  private final UserEventMessageMapper mapper = Mappers.getMapper(UserEventMessageMapper.class);
  private final UUID userId = UUID.randomUUID();

  private static final String NAME = "name";
  private static final String EMAIL = "test@example.com";

  private UserData expectedData() {
    final UserData data = new UserData();
    data.setUserId(userId.toString());
    data.setName(NAME);
    data.setEmail(EMAIL);
    return data;
  }

  private MessageHeader expectedHeader(final MessageHeader actual) {
    Assertions.assertThat(actual.getMessageId()).isNotNull();
    final MessageHeader header = new MessageHeader();
    header.setMessageId(actual.getMessageId());
    header.setMessageDate(OffsetDateTime.now(TestClock.FIXED_CLOCK));
    return header;
  }

  @Test
  void createdHandlerShouldPublishUserCreatedMessage() {
    final UserCreatedEvent event = new UserCreatedEvent();
    event.setUserId(userId);
    event.setName(NAME);
    event.setEmail(EMAIL);

    new UserCreatedEventHandler(publisher, mapper, TestClock.FIXED_CLOCK).handle(event);

    final ArgumentCaptor<UserCreated> captor = ArgumentCaptor.forClass(UserCreated.class);
    Mockito.verify(publisher).publishEvent(captor.capture());
    final UserCreated expected = new UserCreated();
    expected.setMessageHeader(expectedHeader(captor.getValue().getMessageHeader()));
    expected.setUserData(expectedData());
    Assertions.assertThat(captor.getValue()).usingRecursiveComparison().isEqualTo(expected);
  }

  @Test
  void updatedHandlerShouldPublishUserUpdatedMessage() {
    final UserUpdatedEvent event = new UserUpdatedEvent();
    event.setUserId(userId);
    event.setName(NAME);
    event.setEmail(EMAIL);

    new UserUpdatedEventHandler(publisher, mapper, TestClock.FIXED_CLOCK).handle(event);

    final ArgumentCaptor<UserUpdated> captor = ArgumentCaptor.forClass(UserUpdated.class);
    Mockito.verify(publisher).publishEvent(captor.capture());
    final UserUpdated expected = new UserUpdated();
    expected.setMessageHeader(expectedHeader(captor.getValue().getMessageHeader()));
    expected.setUserData(expectedData());
    Assertions.assertThat(captor.getValue()).usingRecursiveComparison().isEqualTo(expected);
  }

  @Test
  void deletedHandlerShouldPublishUserDeletedMessage() {
    final UserDeletedEvent event = new UserDeletedEvent();
    event.setUserId(userId);
    event.setName(NAME);
    event.setEmail(EMAIL);

    new UserDeletedEventHandler(publisher, mapper, TestClock.FIXED_CLOCK).handle(event);

    final ArgumentCaptor<UserDeleted> captor = ArgumentCaptor.forClass(UserDeleted.class);
    Mockito.verify(publisher).publishEvent(captor.capture());
    final UserDeleted expected = new UserDeleted();
    expected.setMessageHeader(expectedHeader(captor.getValue().getMessageHeader()));
    expected.setUserData(expectedData());
    Assertions.assertThat(captor.getValue()).usingRecursiveComparison().isEqualTo(expected);
  }
}
