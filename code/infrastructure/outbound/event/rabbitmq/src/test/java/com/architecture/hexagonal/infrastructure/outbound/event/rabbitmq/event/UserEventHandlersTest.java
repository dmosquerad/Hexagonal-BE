package com.architecture.hexagonal.infrastructure.outbound.event.rabbitmq.event;

import com.architecture.hexagonal.application.testutils.time.TestClock;
import com.architecture.hexagonal.contract.bus.model.user.event.UserCreatedEvent;
import com.architecture.hexagonal.contract.bus.model.user.event.UserDeletedEvent;
import com.architecture.hexagonal.contract.bus.model.user.event.UserUpdatedEvent;
import com.architecture.hexagonal.contract.outbound.event.rabbitmq.model.UserCreated;
import com.architecture.hexagonal.contract.outbound.event.rabbitmq.model.UserDeleted;
import com.architecture.hexagonal.contract.outbound.event.rabbitmq.model.UserUpdated;
import com.architecture.hexagonal.infrastructure.outbound.event.rabbitmq.mapper.user.UserEventMessageMapper;
import com.architecture.hexagonal.infrastructure.outbound.event.rabbitmq.testutils.event.UserCreatedEventTestDataBuilder;
import com.architecture.hexagonal.infrastructure.outbound.event.rabbitmq.testutils.event.UserDeletedEventTestDataBuilder;
import com.architecture.hexagonal.infrastructure.outbound.event.rabbitmq.testutils.event.UserUpdatedEventTestDataBuilder;
import com.architecture.hexagonal.infrastructure.outbound.event.rabbitmq.testutils.message.MessageHeaderTestDataBuilder;
import com.architecture.hexagonal.infrastructure.outbound.event.rabbitmq.testutils.message.UserCreatedTestDataBuilder;
import com.architecture.hexagonal.infrastructure.outbound.event.rabbitmq.testutils.message.UserDataTestDataBuilder;
import com.architecture.hexagonal.infrastructure.outbound.event.rabbitmq.testutils.message.UserDeletedTestDataBuilder;
import com.architecture.hexagonal.infrastructure.outbound.event.rabbitmq.testutils.message.UserUpdatedTestDataBuilder;
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

  @Test
  void createdHandlerShouldPublishUserCreatedMessage() {
    final UserCreatedEvent event =
        UserCreatedEventTestDataBuilder.builder()
            .userId(userId)
            .name(NAME)
            .email(EMAIL)
            .build()
            .userCreatedEvent();

    new UserCreatedEventHandler(publisher, mapper, TestClock.FIXED_CLOCK).handle(event);

    final ArgumentCaptor<UserCreated> captor = ArgumentCaptor.forClass(UserCreated.class);
    Mockito.verify(publisher).publishEvent(captor.capture());
    Assertions.assertThat(captor.getValue().getMessageHeader().getMessageId()).isNotNull();
    final UserCreated expected =
        UserCreatedTestDataBuilder.builder()
            .messageHeader(
                MessageHeaderTestDataBuilder.builder()
                    .messageId(captor.getValue().getMessageHeader().getMessageId())
                    .messageDate(OffsetDateTime.now(TestClock.FIXED_CLOCK))
                    .build()
                    .messageHeader())
            .userData(
                UserDataTestDataBuilder.builder()
                    .userId(userId.toString())
                    .name(NAME)
                    .build()
                    .userData())
            .build()
            .userCreated();
    Assertions.assertThat(captor.getValue()).usingRecursiveComparison().isEqualTo(expected);
  }

  @Test
  void updatedHandlerShouldPublishUserUpdatedMessage() {
    final UserUpdatedEvent event =
        UserUpdatedEventTestDataBuilder.builder()
            .userId(userId)
            .name(NAME)
            .email(EMAIL)
            .build()
            .userUpdatedEvent();

    new UserUpdatedEventHandler(publisher, mapper, TestClock.FIXED_CLOCK).handle(event);

    final ArgumentCaptor<UserUpdated> captor = ArgumentCaptor.forClass(UserUpdated.class);
    Mockito.verify(publisher).publishEvent(captor.capture());
    Assertions.assertThat(captor.getValue().getMessageHeader().getMessageId()).isNotNull();
    final UserUpdated expected =
        UserUpdatedTestDataBuilder.builder()
            .messageHeader(
                MessageHeaderTestDataBuilder.builder()
                    .messageId(captor.getValue().getMessageHeader().getMessageId())
                    .messageDate(OffsetDateTime.now(TestClock.FIXED_CLOCK))
                    .build()
                    .messageHeader())
            .userData(
                UserDataTestDataBuilder.builder()
                    .userId(userId.toString())
                    .name(NAME)
                    .build()
                    .userData())
            .build()
            .userUpdated();
    Assertions.assertThat(captor.getValue()).usingRecursiveComparison().isEqualTo(expected);
  }

  @Test
  void deletedHandlerShouldPublishUserDeletedMessage() {
    final UserDeletedEvent event =
        UserDeletedEventTestDataBuilder.builder()
            .userId(userId)
            .name(NAME)
            .email(EMAIL)
            .build()
            .userDeletedEvent();

    new UserDeletedEventHandler(publisher, mapper, TestClock.FIXED_CLOCK).handle(event);

    final ArgumentCaptor<UserDeleted> captor = ArgumentCaptor.forClass(UserDeleted.class);
    Mockito.verify(publisher).publishEvent(captor.capture());
    Assertions.assertThat(captor.getValue().getMessageHeader().getMessageId()).isNotNull();
    final UserDeleted expected =
        UserDeletedTestDataBuilder.builder()
            .messageHeader(
                MessageHeaderTestDataBuilder.builder()
                    .messageId(captor.getValue().getMessageHeader().getMessageId())
                    .messageDate(OffsetDateTime.now(TestClock.FIXED_CLOCK))
                    .build()
                    .messageHeader())
            .userData(
                UserDataTestDataBuilder.builder()
                    .userId(userId.toString())
                    .name(NAME)
                    .build()
                    .userData())
            .build()
            .userDeleted();
    Assertions.assertThat(captor.getValue()).usingRecursiveComparison().isEqualTo(expected);
  }
}
