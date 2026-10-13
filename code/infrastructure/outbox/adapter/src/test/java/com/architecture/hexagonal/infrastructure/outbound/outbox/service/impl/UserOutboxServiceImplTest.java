package com.architecture.hexagonal.infrastructure.outbound.outbox.service.impl;

import com.architecture.hexagonal.contract.outbound.rabbitmq.UserCreated;
import com.architecture.hexagonal.contract.outbound.rabbitmq.UserData;
import com.architecture.hexagonal.contract.outbound.rabbitmq.UserDeleted;
import com.architecture.hexagonal.contract.outbound.rabbitmq.UserUpdated;
import com.architecture.hexagonal.domain.model.entity.outbox.Outbox;
import com.architecture.hexagonal.domain.model.vo.outbox.ActionType;
import com.architecture.hexagonal.domain.model.vo.outbox.PayloadVo;
import com.architecture.hexagonal.domain.testutils.data.model.entity.outbox.OutboxTestDataBuilder;
import com.architecture.hexagonal.domain.testutils.data.model.vo.outbox.PayloadVoTestDataBuilder;
import com.architecture.hexagonal.infrastructure.outbound.outbox.mapper.user.UserFromOutboxMapper;
import com.architecture.hexagonal.infrastructure.outbound.outbox.naming.OutboxNaming;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

@ExtendWith(MockitoExtension.class)
class UserOutboxServiceImplTest {

  @InjectMocks private UserOutboxServiceImpl userOutboxServiceImpl;

  @Mock private ApplicationEventPublisher applicationEventPublisher;

  @Spy
  private final UserFromOutboxMapper userFromOutboxMapper =
      Mappers.getMapper(UserFromOutboxMapper.class);

  @Test
  void processShouldPublishCreatedEventWhenActionIsUserCreated() {
    final PayloadVo payload = PayloadVoTestDataBuilder.<UserData>builder().build().payloadVo();
    final Outbox outbox =
        OutboxTestDataBuilder.builder()
            .action(ActionType.UserActionType.USER_CREATED.getActionType())
            .payload(payload)
            .build()
            .outbox();

    userOutboxServiceImpl.process(outbox);

    Mockito.verify(userFromOutboxMapper).toUserCreated(payload);
    Mockito.verify(applicationEventPublisher).publishEvent(Mockito.any(UserCreated.class));
  }

  @Test
  void processShouldPublishUpdatedEventWhenActionIsUserUpdated() {
    final PayloadVo payload = PayloadVoTestDataBuilder.<UserData>builder().build().payloadVo();
    final Outbox outbox =
        OutboxTestDataBuilder.builder()
            .action(ActionType.UserActionType.USER_UPDATED.getActionType())
            .payload(payload)
            .build()
            .outbox();

    userOutboxServiceImpl.process(outbox);

    Mockito.verify(userFromOutboxMapper).toUserUpdated(payload);
    Mockito.verify(applicationEventPublisher).publishEvent(Mockito.any(UserUpdated.class));
  }

  @Test
  void processShouldPublishDeletedEventWhenActionIsUserDeleted() {
    final PayloadVo payload = PayloadVoTestDataBuilder.<UserData>builder().build().payloadVo();
    final Outbox outbox =
        OutboxTestDataBuilder.builder()
            .action(ActionType.UserActionType.USER_DELETED.getActionType())
            .payload(payload)
            .build()
            .outbox();

    userOutboxServiceImpl.process(outbox);

    Mockito.verify(userFromOutboxMapper).toUserDeleted(payload);
    Mockito.verify(applicationEventPublisher).publishEvent(Mockito.any(UserDeleted.class));
  }

  @Test
  void processShouldThrowIllegalArgumentExceptionWhenActionIsUnsupported() {
    final Outbox outbox = OutboxTestDataBuilder.builder().action("").payload(null).build().outbox();

    AssertionsForClassTypes.assertThatThrownBy(() -> userOutboxServiceImpl.process(outbox))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining(OutboxNaming.UNSUPPORTED_USER_ACTION);

    Mockito.verifyNoInteractions(applicationEventPublisher);
  }
}
