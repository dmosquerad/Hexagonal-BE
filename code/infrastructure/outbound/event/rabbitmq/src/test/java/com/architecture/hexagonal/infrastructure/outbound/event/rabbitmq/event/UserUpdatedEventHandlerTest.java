package com.architecture.hexagonal.infrastructure.outbound.event.rabbitmq.event;

import com.architecture.hexagonal.application.testutils.time.TestClock;
import com.architecture.hexagonal.contract.bus.model.user.event.UserUpdatedEvent;
import com.architecture.hexagonal.contract.outbound.event.rabbitmq.model.UserUpdated;
import com.architecture.hexagonal.infrastructure.outbound.event.rabbitmq.mapper.user.UserEventMessageMapper;
import com.architecture.hexagonal.infrastructure.outbound.event.rabbitmq.testutils.event.UserUpdatedEventTestDataBuilder;
import com.architecture.hexagonal.infrastructure.outbound.event.rabbitmq.testutils.message.MessageHeaderTestDataBuilder;
import com.architecture.hexagonal.infrastructure.outbound.event.rabbitmq.testutils.message.UserDataTestDataBuilder;
import com.architecture.hexagonal.infrastructure.outbound.event.rabbitmq.testutils.message.UserUpdatedTestDataBuilder;
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
class UserUpdatedEventHandlerTest {

  @InjectMocks private UserUpdatedEventHandler userUpdatedEventHandler;

  @Mock private ApplicationEventPublisher publisher;

  @Spy private UserEventMessageMapper mapper = Mappers.getMapper(UserEventMessageMapper.class);

  @Spy private Clock clock = TestClock.FIXED_CLOCK;

  @Test
  void updatedHandlerShouldPublishUserUpdatedMessage() {
    final UserUpdatedEvent event =
        UserUpdatedEventTestDataBuilder.builder().build().userUpdatedEvent();

    userUpdatedEventHandler.handle(event);

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
                    .userId(event.getUserId().toString())
                    .build()
                    .userData())
            .build()
            .userUpdated();

    Assertions.assertThat(captor.getValue()).usingRecursiveComparison().isEqualTo(expected);
  }
}
