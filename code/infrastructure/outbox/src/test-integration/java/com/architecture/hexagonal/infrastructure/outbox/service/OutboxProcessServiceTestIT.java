package com.architecture.hexagonal.infrastructure.outbox.service;

import com.architecture.hexagonal.contract.outbound.event.rabbitmq.model.UserCreated;
import com.architecture.hexagonal.contract.outbound.event.rabbitmq.model.UserDeleted;
import com.architecture.hexagonal.contract.outbound.event.rabbitmq.model.UserUpdated;
import com.architecture.hexagonal.domain.model.entity.outbox.Outbox;
import com.architecture.hexagonal.domain.model.vo.payload.ActionType;
import com.architecture.hexagonal.domain.model.vo.payload.AggregateTypeVo;
import com.architecture.hexagonal.domain.testutils.data.model.entity.outbox.OutboxTestDataBuilder;
import com.architecture.hexagonal.domain.testutils.data.model.vo.payload.PayloadVoTestDataBuilder;
import com.architecture.hexagonal.infrastructure.outbox.config.TestApplication;
import com.architecture.hexagonal.infrastructure.outbox.mapper.user.UserFromOutboxMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

@SpringBootTest(classes = {OutboxProcessService.class})
@ContextConfiguration(classes = {TestApplication.class})
class OutboxProcessServiceTestIT {

  @Autowired private OutboxProcessService outboxProcessService;

  @Autowired @MockitoBean private ApplicationEventPublisher applicationEventPublisher;

  @MockitoSpyBean private UserFromOutboxMapper userFromOutboxMapper;

  @Test
  void processShouldPublishCreatedEvent() {
    final Outbox outbox =
        outbox(ActionType.UserActionType.USER_CREATED.getActionType());

    outboxProcessService.process(outbox);

    Mockito.verify(userFromOutboxMapper).toUserCreated(outbox.payload());
    Mockito.verify(applicationEventPublisher).publishEvent(Mockito.any(UserCreated.class));
  }

  @Test
  void processShouldPublishUpdatedEvent() {
    final Outbox outbox =
        outbox(ActionType.UserActionType.USER_UPDATED.getActionType());

    outboxProcessService.process(outbox);

    Mockito.verify(userFromOutboxMapper).toUserUpdated(outbox.payload());
    Mockito.verify(applicationEventPublisher).publishEvent(Mockito.any(UserUpdated.class));
  }

  @Test
  void processShouldPublishDeletedEvent() {
    final Outbox outbox =
        outbox(ActionType.UserActionType.USER_DELETED.getActionType());

    outboxProcessService.process(outbox);

    Mockito.verify(userFromOutboxMapper).toUserDeleted(outbox.payload());
    Mockito.verify(applicationEventPublisher).publishEvent(Mockito.any(UserDeleted.class));
  }

  private Outbox outbox(final String action) {
    return OutboxTestDataBuilder.builder()
        .aggregateType(AggregateTypeVo.USER)
        .action(action)
        .payload(PayloadVoTestDataBuilder.<Object>builder().build().payloadVo())
        .build()
        .outbox();
  }
}
