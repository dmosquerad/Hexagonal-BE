package com.architecture.hexagonal.infrastructure.outbound.message.rabbitmq.sender;

import com.architecture.hexagonal.application.port.database.OutboxRepositoryWritePort;
import com.architecture.hexagonal.domain.model.aggregate.outbox.Outbox;
import com.architecture.hexagonal.domain.model.vo.OutboxStatusVo;
import com.architecture.hexagonal.infrastructure.outbound.message.rabbitmq.data.UserCreated;
import com.architecture.hexagonal.infrastructure.outbound.message.rabbitmq.data.UserDeleted;
import com.architecture.hexagonal.infrastructure.outbound.message.rabbitmq.data.UserUpdated;
import com.architecture.hexagonal.infrastructure.outbound.message.rabbitmq.naming.UserMessageNaming;
import java.time.Clock;
import java.time.OffsetDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class UserMessageSender {

  private final StreamBridge streamBridge;
  private final OutboxRepositoryWritePort outboxRepositoryWritePort;
  private final Clock clock;

  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  public void sendUserCreatedMessage(final UserCreated event) {
    try {
      streamBridge.send(UserMessageNaming.PUBLISH_USER_CREATED_OUT_BINDING, event);
    } catch (Exception ex) {
      outboxRepositoryWritePort.save(
          Outbox.builder()
              .aggregateId(event.getUserId())
              .aggregateType(UserMessageNaming.AGGREGATE_USER_TYPE)
              .action(UserMessageNaming.ACTION_USER_CREATED)
              .status(OutboxStatusVo.PENDING)
              .payload(event)
              .retryCount(0)
              .createdAt(OffsetDateTime.now(clock))
              .build());
    }
  }

  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  public void sendUserUpdatedMessage(final UserUpdated event) {
    try {
      streamBridge.send(UserMessageNaming.PUBLISH_USER_UPDATED_OUT_BINDING, event);
    } catch (Exception ex) {
      outboxRepositoryWritePort.save(
          Outbox.builder()
              .aggregateId(event.getUserId())
              .aggregateType(UserMessageNaming.AGGREGATE_USER_TYPE)
              .action(UserMessageNaming.ACTION_USER_UPDATED)
              .status(OutboxStatusVo.PENDING)
              .payload(event)
              .retryCount(0)
              .createdAt(OffsetDateTime.now(clock))
              .build());
    }
  }

  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  public void sendUserDeletedMessage(final UserDeleted event) {
    try {
      streamBridge.send(UserMessageNaming.PUBLISH_USER_DELETED_OUT_BINDING, event);
    } catch (Exception ex) {
      outboxRepositoryWritePort.save(
          Outbox.builder()
              .aggregateId(event.getUserId())
              .aggregateType(UserMessageNaming.AGGREGATE_USER_TYPE)
              .action(UserMessageNaming.ACTION_USER_DELETED)
              .status(OutboxStatusVo.PENDING)
              .payload(event)
              .retryCount(0)
              .createdAt(OffsetDateTime.now(clock))
              .build());
    }
  }
}
