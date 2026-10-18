package com.architecture.hexagonal.infrastructure.outbound.event.rabbitmq.producer;

import com.architecture.hexagonal.application.port.out.outbox.OutboxRepositoryWritePort;
import com.architecture.hexagonal.contract.outbound.rabbitmq.UserEventsProducer;
import com.architecture.hexagonal.contract.outbound.rabbitmq.model.UserCreated;
import com.architecture.hexagonal.contract.outbound.rabbitmq.model.UserDeleted;
import com.architecture.hexagonal.contract.outbound.rabbitmq.model.UserUpdated;
import com.architecture.hexagonal.domain.model.entity.outbox.Outbox;
import com.architecture.hexagonal.domain.model.vo.payload.ActionType;
import com.architecture.hexagonal.domain.model.vo.payload.AggregateTypeVo;
import com.architecture.hexagonal.domain.model.vo.payload.StatusVo;
import com.architecture.hexagonal.infrastructure.outbound.event.rabbitmq.mapper.user.PayloadMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class UserEventsProducerImpl implements UserEventsProducer {

  private final StreamBridge streamBridge;
  private final PayloadMapper payloadMapper;
  private final OutboxRepositoryWritePort outboxRepositoryWritePort;

  @Override
  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  public boolean publishUserCreated(final UserCreated payload) {
    if (tryPublishUserCreated(payload)) {
      return true;
    }

    outboxRepositoryWritePort.upsertByEventIdentity(
        Outbox.builder()
            .aggregateId(payload.getData().getUserId())
            .aggregateType(AggregateTypeVo.USER)
            .action(ActionType.UserActionType.USER_CREATED.getActionType())
            .status(StatusVo.PENDING)
            .payload(payloadMapper.toPayload(payload))
            .createdAt(payload.getMessageHeader().getMessageDate())
            .build());
    return false;
  }

  private boolean tryPublishUserCreated(final UserCreated payload) {
    try {
      return streamBridge.send(
          "publish-user-created-out-0", MessageBuilder.withPayload(payload).build());
    } catch (final RuntimeException _) {
      return false;
    }
  }

  @Override
  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  public boolean publishUserUpdated(final UserUpdated payload) {
    if (tryPublishUserUpdated(payload)) {
      return true;
    }

    outboxRepositoryWritePort.upsertByEventIdentity(
        Outbox.builder()
            .aggregateId(payload.getData().getUserId())
            .aggregateType(AggregateTypeVo.USER)
            .action(ActionType.UserActionType.USER_UPDATED.getActionType())
            .status(StatusVo.PENDING)
            .payload(payloadMapper.toPayload(payload))
            .createdAt(payload.getMessageHeader().getMessageDate())
            .build());
    return false;
  }

  private boolean tryPublishUserUpdated(final UserUpdated payload) {
    try {
      return streamBridge.send(
          "publish-user-updated-out-0", MessageBuilder.withPayload(payload).build());
    } catch (final RuntimeException _) {
      return false;
    }
  }

  @Override
  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  public boolean publishUserDeleted(final UserDeleted payload) {
    if (tryPublishUserDeleted(payload)) {
      return true;
    }

    outboxRepositoryWritePort.upsertByEventIdentity(
        Outbox.builder()
            .aggregateId(payload.getData().getUserId())
            .aggregateType(AggregateTypeVo.USER)
            .action(ActionType.UserActionType.USER_DELETED.getActionType())
            .status(StatusVo.PENDING)
            .payload(payloadMapper.toPayload(payload))
            .createdAt(payload.getMessageHeader().getMessageDate())
            .build());
    return false;
  }

  private boolean tryPublishUserDeleted(final UserDeleted payload) {
    try {
      return streamBridge.send(
          "publish-user-deleted-out-0", MessageBuilder.withPayload(payload).build());
    } catch (final RuntimeException _) {
      return false;
    }
  }
}
