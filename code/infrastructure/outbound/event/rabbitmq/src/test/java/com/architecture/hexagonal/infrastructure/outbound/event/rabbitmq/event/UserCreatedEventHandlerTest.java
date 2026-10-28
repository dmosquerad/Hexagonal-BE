package com.architecture.hexagonal.infrastructure.outbound.event.rabbitmq.event;

import com.architecture.hexagonal.application.testutils.time.TestClock;
import com.architecture.hexagonal.contract.bus.model.user.event.UserCreatedEvent;
import com.architecture.hexagonal.contract.outbound.event.rabbitmq.model.UserCreated;
import com.architecture.hexagonal.infrastructure.outbound.event.rabbitmq.mapper.user.UserEventMessageMapper;
import com.architecture.hexagonal.infrastructure.outbound.event.rabbitmq.testutils.event.UserCreatedEventTestDataBuilder;
import com.architecture.hexagonal.infrastructure.outbound.event.rabbitmq.testutils.message.MessageHeaderTestDataBuilder;
import com.architecture.hexagonal.infrastructure.outbound.event.rabbitmq.testutils.message.UserCreatedTestDataBuilder;
import com.architecture.hexagonal.infrastructure.outbound.event.rabbitmq.testutils.message.UserDataTestDataBuilder;
import java.time.Clock;
import java.time.OffsetDateTime;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

@ExtendWith(MockitoExtension.class)
class UserCreatedEventHandlerTest {

  @InjectMocks private UserCreatedEventHandler userCreatedEventHandler;

  @Mock private ApplicationEventPublisher publisher;

  @Spy private UserEventMessageMapper mapper = Mappers.getMapper(UserEventMessageMapper.class);

  @Spy private Clock clock = TestClock.FIXED_CLOCK;

  @Test
  void createdHandlerShouldPublishUserCreatedMessage() {
    final UserCreatedEvent event =
        UserCreatedEventTestDataBuilder.builder().build().userCreatedEvent();

    userCreatedEventHandler.handle(event);

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
                    .userId(event.getUserId().toString())
                    .build()
                    .userData())
            .build()
            .userCreated();

    Assertions.assertThat(captor.getValue()).usingRecursiveComparison().isEqualTo(expected);
  }
}
