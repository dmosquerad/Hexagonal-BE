package com.architecture.hexagonal.infrastructure.outbound.event.rabbitmq.event;

import com.architecture.hexagonal.contract.outbound.event.rabbitmq.model.UserUpdated;
import com.architecture.hexagonal.domain.databuilder.model.event.user.UserUpdatedEventTestDataBuilder;
import com.architecture.hexagonal.domain.databuilder.model.vo.time.TestTime;
import com.architecture.hexagonal.domain.model.event.user.UserUpdatedEvent;
import com.architecture.hexagonal.infrastructure.outbound.event.rabbitmq.databuilder.message.MessageHeaderTestDataBuilder;
import com.architecture.hexagonal.infrastructure.outbound.event.rabbitmq.databuilder.message.UserDataTestDataBuilder;
import com.architecture.hexagonal.infrastructure.outbound.event.rabbitmq.databuilder.message.UserUpdatedTestDataBuilder;
import com.architecture.hexagonal.infrastructure.outbound.event.rabbitmq.mapper.user.UserUpdatedEventMessageMapper;
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
class UserUpdatedEventPublisherTest {

  @InjectMocks private UserUpdatedEventPublisher userUpdatedEventHandler;

  @Mock private ApplicationEventPublisher publisher;

  @Spy
  private UserUpdatedEventMessageMapper mapper =
      Mappers.getMapper(UserUpdatedEventMessageMapper.class);

  @Spy private Clock clock = TestTime.FIXED_CLOCK;

  @Test
  void updatedHandlerShouldPublishUserUpdatedMessage() {
    final UserUpdatedEvent event =
        UserUpdatedEventTestDataBuilder.builder().build().userUpdatedEvent();

    userUpdatedEventHandler.publish(event);

    final ArgumentCaptor<UserUpdated> captor = ArgumentCaptor.forClass(UserUpdated.class);
    Mockito.verify(publisher).publishEvent(captor.capture());
    Assertions.assertThat(captor.getValue().getMessageHeader().getMessageId()).isNotNull();
    final UserUpdated expected =
        UserUpdatedTestDataBuilder.builder()
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
            .userUpdated();

    Assertions.assertThat(captor.getValue()).usingRecursiveComparison().isEqualTo(expected);
  }
}
