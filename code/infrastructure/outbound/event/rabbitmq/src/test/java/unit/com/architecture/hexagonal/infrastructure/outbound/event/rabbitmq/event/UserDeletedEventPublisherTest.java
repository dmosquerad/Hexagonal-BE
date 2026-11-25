package com.architecture.hexagonal.infrastructure.outbound.event.rabbitmq.event;

import com.architecture.hexagonal.contract.bus.model.user.event.UserDeletedEvent;
import com.architecture.hexagonal.contract.outbound.event.rabbitmq.model.UserDeleted;
import com.architecture.hexagonal.domain.databuilder.model.vo.time.TestTime;
import com.architecture.hexagonal.infrastructure.outbound.event.rabbitmq.databuilder.event.UserDeletedEventTestDataBuilder;
import com.architecture.hexagonal.infrastructure.outbound.event.rabbitmq.databuilder.message.MessageHeaderTestDataBuilder;
import com.architecture.hexagonal.infrastructure.outbound.event.rabbitmq.databuilder.message.UserDataTestDataBuilder;
import com.architecture.hexagonal.infrastructure.outbound.event.rabbitmq.databuilder.message.UserDeletedTestDataBuilder;
import com.architecture.hexagonal.infrastructure.outbound.event.rabbitmq.mapper.user.UserDeletedEventMessageMapper;
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
class UserDeletedEventPublisherTest {

  @InjectMocks private UserDeletedEventPublisher userDeletedEventHandler;

  @Mock private ApplicationEventPublisher publisher;

  @Spy
  private UserDeletedEventMessageMapper mapper =
      Mappers.getMapper(UserDeletedEventMessageMapper.class);

  @Spy private Clock clock = TestTime.FIXED_CLOCK;

  @Test
  void deletedHandlerShouldPublishUserDeletedMessage() {
    final UserDeletedEvent event =
        UserDeletedEventTestDataBuilder.builder().build().userDeletedEvent();

    userDeletedEventHandler.publish(event);

    final ArgumentCaptor<UserDeleted> captor = ArgumentCaptor.forClass(UserDeleted.class);
    Mockito.verify(publisher).publishEvent(captor.capture());
    Assertions.assertThat(captor.getValue().getMessageHeader().getMessageId()).isNotNull();
    final UserDeleted expected =
        UserDeletedTestDataBuilder.builder()
            .messageHeader(
                MessageHeaderTestDataBuilder.builder()
                    .messageId(captor.getValue().getMessageHeader().getMessageId())
                    .messageDate(OffsetDateTime.now(TestTime.FIXED_CLOCK))
                    .build()
                    .messageHeader())
            .userData(
                UserDataTestDataBuilder.builder()
                    .userId(event.getUserId().toString())
                    .build()
                    .userData())
            .build()
            .userDeleted();

    Assertions.assertThat(captor.getValue()).usingRecursiveComparison().isEqualTo(expected);
  }
}
