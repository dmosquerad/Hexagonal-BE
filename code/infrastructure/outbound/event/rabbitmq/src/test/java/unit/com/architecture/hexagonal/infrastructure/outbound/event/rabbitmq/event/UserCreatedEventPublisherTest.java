package com.architecture.hexagonal.infrastructure.outbound.event.rabbitmq.event;

import com.architecture.hexagonal.contract.outbound.event.rabbitmq.model.UserCreated;
import com.architecture.hexagonal.domain.databuilder.model.event.user.UserCreatedEventTestDataBuilder;
import com.architecture.hexagonal.domain.databuilder.model.vo.time.TestTime;
import com.architecture.hexagonal.domain.model.event.user.UserCreatedEvent;
import com.architecture.hexagonal.infrastructure.outbound.event.rabbitmq.databuilder.message.MessageHeaderTestDataBuilder;
import com.architecture.hexagonal.infrastructure.outbound.event.rabbitmq.databuilder.message.UserCreatedTestDataBuilder;
import com.architecture.hexagonal.infrastructure.outbound.event.rabbitmq.databuilder.message.UserDataTestDataBuilder;
import com.architecture.hexagonal.infrastructure.outbound.event.rabbitmq.mapper.user.UserCreatedEventMessageMapper;
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
class UserCreatedEventPublisherTest {

  @InjectMocks private UserCreatedEventPublisher userCreatedEventHandler;

  @Mock private ApplicationEventPublisher publisher;

  @Spy
  private UserCreatedEventMessageMapper mapper =
      Mappers.getMapper(UserCreatedEventMessageMapper.class);

  @Spy private Clock clock = TestTime.FIXED_CLOCK;

  @Test
  void createdHandlerShouldPublishUserCreatedMessage() {
    final UserCreatedEvent event =
        UserCreatedEventTestDataBuilder.builder().build().userCreatedEvent();

    userCreatedEventHandler.publish(event);

    final ArgumentCaptor<UserCreated> captor = ArgumentCaptor.forClass(UserCreated.class);
    Mockito.verify(publisher).publishEvent(captor.capture());
    Assertions.assertThat(captor.getValue().getMessageHeader().getMessageId()).isNotNull();
    final UserCreated expected =
        UserCreatedTestDataBuilder.builder()
            .messageHeader(
                MessageHeaderTestDataBuilder.builder()
                    .messageId(captor.getValue().getMessageHeader().getMessageId())
                    .messageDate(OffsetDateTime.now(TestTime.FIXED_CLOCK))
                    .build()
                    .messageHeader())
            .userData(
                UserDataTestDataBuilder.builder()
                    .userId(event.userId().toString())
                    .build()
                    .userData())
            .build()
            .userCreated();

    Assertions.assertThat(captor.getValue()).usingRecursiveComparison().isEqualTo(expected);
  }
}
