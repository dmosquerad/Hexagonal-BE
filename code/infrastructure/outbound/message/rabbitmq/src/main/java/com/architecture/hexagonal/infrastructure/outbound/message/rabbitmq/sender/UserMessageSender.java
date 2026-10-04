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
  public void sendUserCreatedMessage(final UserCreated userCreated) {
    if (isSendUserCreatedMessage(userCreated)) {
      return;
    }

    outboxRepositoryWritePort.upsertByEventIdentity(
        Outbox.builder()
            .aggregateId(userCreated.getData().getId())
            .aggregateType(AggregateTypeVo.USER)
            .action(ActionType.UserActionType.USER_CREATED.getActionType())
            .status(OutboxStatusVo.PENDING)
            .payload(payloadMapper.toPayload(userCreated))
            .createdAt(userCreated.getMessageHeader().messageDate())
            .build());
  }

  private boolean isSendUserCreatedMessage(final UserCreated userCreated) {
    try {
      return streamBridge.send(UserPublishBinding.USER_CREATED.getPublishBinding(), userCreated);
    } catch (Exception _) {
      return false;
    }
  }

  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  public void sendUserUpdatedMessage(final UserUpdated userUpdated) {
    if (isSendUserUpdatedMessage(userUpdated)) {
      return;
    }

    outboxRepositoryWritePort.upsertByEventIdentity(
        Outbox.builder()
            .aggregateId(userUpdated.getData().getId())
            .aggregateType(AggregateTypeVo.USER)
            .action(ActionType.UserActionType.USER_UPDATED.getActionType())
            .status(OutboxStatusVo.PENDING)
            .payload(payloadMapper.toPayload(userUpdated))
            .createdAt(userUpdated.getMessageHeader().messageDate())
            .build());
  }

  private boolean isSendUserUpdatedMessage(final UserUpdated userUpdated) {
    try {
      return streamBridge.send(UserPublishBinding.USER_UPDATED.getPublishBinding(), userUpdated);
    } catch (Exception _) {
      return false;
    }
  }

  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  public void sendUserDeletedMessage(final UserDeleted userDeleted) {
    if (isSendUserDeletedMessage(userDeleted)) {
      return;
    }
    outboxRepositoryWritePort.upsertByEventIdentity(
        Outbox.builder()
            .aggregateId(userDeleted.getData().getId())
            .aggregateType(AggregateTypeVo.USER)
            .action(ActionType.UserActionType.USER_DELETED.getActionType())
            .status(OutboxStatusVo.PENDING)
            .payload(payloadMapper.toPayload(userDeleted))
            .createdAt(userDeleted.getMessageHeader().messageDate())
            .build());
  }

  private boolean isSendUserDeletedMessage(final UserDeleted userDeleted) {
    try {
      return streamBridge.send(UserPublishBinding.USER_DELETED.getPublishBinding(), userDeleted);
    } catch (Exception _) {
      return false;
    }
  }
}
