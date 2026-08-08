package com.architecture.hexagonal.infrastructure.outbound.message.outbox.adapter;

import com.architecture.hexagonal.application.port.message.UserSenderPort;
import com.architecture.hexagonal.domain.model.aggregate.outbox.Outbox;
import com.architecture.hexagonal.domain.model.aggregate.user.User;
import com.architecture.hexagonal.infrastructure.outbound.message.outbox.config.TestApplication;
import com.architecture.hexagonal.infrastructure.outbound.message.outbox.service.OutboxService;
import com.architecture.hexagonal.infrastructure.outbound.message.outbox.testutils.data.aggregate.outbox.OutboxTestDataBuilder;
import com.architecture.hexagonal.infrastructure.outbound.message.outbox.testutils.data.message.UserCreatedTestDataBuilder;
import com.architecture.hexagonal.infrastructure.outbound.message.outbox.testutils.data.message.UserDeletedTestDataBuilder;
import com.architecture.hexagonal.infrastructure.outbound.message.outbox.testutils.data.message.UserUpdatedTestDataBuilder;
import com.architecture.hexagonal.infrastructure.outbound.message.rabbitmq.naming.UserMessageNaming;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

@SpringBootTest(classes = {OutboxProcessAdapterImpl.class})
@ContextConfiguration(classes = TestApplication.class)
class OutboxProcessAdapterImplTestIT {

  @Autowired private OutboxProcessAdapterImpl outboxProcessAdapterImpl;

  @MockitoSpyBean private OutboxService outboxService;

  @MockitoBean
  private UserSenderPort userSenderPort;

  @Test
  void process_shouldSendCreatedMessage_whenActionIsUserCreated() {
    final Outbox outbox =
        OutboxTestDataBuilder.builder()
            .action(UserMessageNaming.ACTION_USER_CREATED)
            .payload(UserCreatedTestDataBuilder.builder().build().userCreated())
            .build()
            .outbox();

    outboxProcessAdapterImpl.process(outbox);

    Mockito.verify(outboxService).process(outbox);
    Mockito.verify(userSenderPort).userSenderCreated(ArgumentMatchers.any(User.class));
  }

  @Test
  void process_shouldSendUpdatedMessage_whenActionIsUserUpdated() {
    final Outbox outbox =
        OutboxTestDataBuilder.builder()
            .action(UserMessageNaming.ACTION_USER_UPDATED)
            .payload(UserUpdatedTestDataBuilder.builder().build().userUpdated())
            .build()
            .outbox();

      outboxProcessAdapterImpl.process(outbox);

      Mockito.verify(outboxService).process(outbox);
      Mockito.verify(userSenderPort).userSenderUpdated(ArgumentMatchers.any(User.class));
  }

  @Test
  void process_shouldSendDeletedMessage_whenActionIsUserDeleted() {
    final Outbox outbox =
        OutboxTestDataBuilder.builder()
            .action(UserMessageNaming.ACTION_USER_DELETED)
            .payload(UserDeletedTestDataBuilder.builder().build().userDeleted())
            .build()
            .outbox();

      outboxProcessAdapterImpl.process(outbox);

      Mockito.verify(outboxService).process(outbox);
      Mockito.verify(userSenderPort).userSenderDeleted(ArgumentMatchers.any(User.class));
  }
}
