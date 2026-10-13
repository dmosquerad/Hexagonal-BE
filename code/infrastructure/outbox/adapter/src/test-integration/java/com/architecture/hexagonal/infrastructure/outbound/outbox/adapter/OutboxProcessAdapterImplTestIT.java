package com.architecture.hexagonal.infrastructure.outbound.outbox.adapter;

import com.architecture.hexagonal.domain.model.entity.outbox.Outbox;
import com.architecture.hexagonal.infrastructure.outbound.outbox.config.TestApplication;
import com.architecture.hexagonal.infrastructure.outbound.outbox.mapper.user.UserFromOutboxMapper;
import com.architecture.hexagonal.infrastructure.outbound.outbox.service.OutboxService;
import com.architecture.hexagonal.infrastructure.outbound.outbox.testutils.model.entity.outbox.OutboxTestDataBuilder;
import com.architecture.hexagonal.infrastructure.outbound.outbox.testutils.model.entity.outbox.PayloadTestDataBuilder;
import com.architecture.hexagonal.contract.outbound.rabbitmq.UserCreated;
import com.architecture.hexagonal.contract.outbound.rabbitmq.UserDeleted;
import com.architecture.hexagonal.contract.outbound.rabbitmq.UserUpdated;
import com.architecture.hexagonal.domain.model.vo.outbox.ActionType;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

@SpringBootTest(classes = {OutboxProcessAdapterImpl.class})
@ContextConfiguration(classes = {TestApplication.class})
class OutboxProcessAdapterImplTestIT {

  @Autowired private OutboxProcessAdapterImpl outboxProcessAdapterImpl;

  @MockitoSpyBean private OutboxService outboxService;

  @Autowired @MockitoBean private ApplicationEventPublisher applicationEventPublisher;

  @MockitoSpyBean private UserFromOutboxMapper userFromOutboxMapper;

  @Test
  void processShouldSendCreatedMessageWhenActionIsUserCreated() {
     final Outbox outbox =
        OutboxTestDataBuilder.builder()
            .action(ActionType.UserActionType.USER_CREATED.getActionType())
            .payload(PayloadTestDataBuilder.builder().build().payload())
            .build()
            .outbox();

    outboxProcessAdapterImpl.process(outbox);
    Mockito.verify(outboxService).process(outbox);
    Mockito.verify(userFromOutboxMapper).toUserCreated(outbox.payload());
    Mockito.verify(applicationEventPublisher).publishEvent(Mockito.any(UserCreated.class));
  }

  @Test
  void processShouldSendUpdatedMessageWhenActionIsUserUpdated() {
     final Outbox outbox =
        OutboxTestDataBuilder.builder()
            .action(ActionType.UserActionType.USER_UPDATED.getActionType())
            .payload(PayloadTestDataBuilder.builder().build().payload())
            .build()
            .outbox();

      outboxProcessAdapterImpl.process(outbox);
      Mockito.verify(outboxService).process(outbox);
      Mockito.verify(userFromOutboxMapper).toUserUpdated(outbox.payload());
      Mockito.verify(applicationEventPublisher).publishEvent(Mockito.any(UserUpdated.class));
  }

  @Test
  void processShouldSendDeletedMessageWhenActionIsUserDeleted() {
     final Outbox outbox =
        OutboxTestDataBuilder.builder()
            .action(ActionType.UserActionType.USER_DELETED.getActionType())
            .payload(PayloadTestDataBuilder.builder().build().payload())
            .build()
            .outbox();

      outboxProcessAdapterImpl.process(outbox);
      Mockito.verify(outboxService).process(outbox);  
      Mockito.verify(userFromOutboxMapper).toUserDeleted(outbox.payload());
      Mockito.verify(applicationEventPublisher).publishEvent(Mockito.any(UserDeleted.class));
  }
}
