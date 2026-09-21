package com.architecture.hexagonal.infrastructure.outbound.message.rabbitmq.sender;

import com.architecture.hexagonal.application.port.database.OutboxRepositoryWritePort;
import com.architecture.hexagonal.domain.model.entity.outbox.Outbox;
import com.architecture.hexagonal.domain.model.vo.outbox.ActionType;
import com.architecture.hexagonal.domain.model.vo.outbox.AggregateTypeVo;
import com.architecture.hexagonal.domain.model.vo.outbox.OutboxStatusVo;
import com.architecture.hexagonal.infrastructure.outbound.message.rabbitmq.data.UserCreated;
import com.architecture.hexagonal.infrastructure.outbound.message.rabbitmq.data.UserDeleted;
import com.architecture.hexagonal.infrastructure.outbound.message.rabbitmq.data.UserUpdated;
import com.architecture.hexagonal.infrastructure.outbound.message.rabbitmq.mapper.user.PayloadMapper;
import com.architecture.hexagonal.infrastructure.outbound.message.rabbitmq.naming.UserPublishBinding;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class UserMessageSender {

  private final StreamBridge streamBridge;
  private final PayloadMapper payloadMapper;
  private final OutboxRepositoryWritePort outboxRepositoryWritePort;

  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  public void sendUserCreatedMessage(final UserCreated event) {
    try {
      streamBridge.send(UserPublishBinding.USER_CREATED.getPublishBinding(), event);
    } catch (Exception ex) {
      outboxRepositoryWritePort.save(
          Outbox.builder()
              .aggregateId(event.getData().getId())
              .aggregateType(AggregateTypeVo.USER)
              .action(ActionType.UserActionType.USER_CREATED.getActionType())
              .status(OutboxStatusVo.PENDING)
              .payload(payloadMapper.toPayload(event))
              .createdAt(event.getMessageHeader().messageDate())
              .build());
    }
  }

  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  public void sendUserUpdatedMessage(final UserUpdated event) {
    try {
      streamBridge.send(UserPublishBinding.USER_UPDATED.getPublishBinding(), event);
    } catch (Exception ex) {
      outboxRepositoryWritePort.save(
          Outbox.builder()
              .aggregateId(event.getData().getId())
              .aggregateType(AggregateTypeVo.USER)
              .action(ActionType.UserActionType.USER_UPDATED.getActionType())
              .status(OutboxStatusVo.PENDING)
              .payload(payloadMapper.toPayload(event))
              .createdAt(event.getMessageHeader().messageDate())
              .build());
    }
  }

  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  public void sendUserDeletedMessage(final UserDeleted event) {
    try {
      streamBridge.send(UserPublishBinding.USER_DELETED.getPublishBinding(), event);
    } catch (Exception ex) {
      outboxRepositoryWritePort.save(
          Outbox.builder()
              .aggregateId(event.getData().getId())
              .aggregateType(AggregateTypeVo.USER)
              .action(ActionType.UserActionType.USER_DELETED.getActionType())
              .status(OutboxStatusVo.PENDING)
              .payload(payloadMapper.toPayload(event))
              .createdAt(event.getMessageHeader().messageDate())
              .build());
    }
  }
}
