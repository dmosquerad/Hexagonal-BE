package com.architecture.hexagonal.infrastructure.outbound.message.rabbitmq.sender;

import com.architecture.hexagonal.application.port.out.outbox.OutboxRepositoryWritePort;
import com.architecture.hexagonal.contract.outbound.rabbitmq.IUserEventsProducer;
import com.architecture.hexagonal.contract.outbound.rabbitmq.UserCreated;
import com.architecture.hexagonal.contract.outbound.rabbitmq.UserDeleted;
import com.architecture.hexagonal.contract.outbound.rabbitmq.UserUpdated;
import com.architecture.hexagonal.domain.model.entity.outbox.Outbox;
import com.architecture.hexagonal.domain.model.vo.outbox.ActionType;
import com.architecture.hexagonal.domain.model.vo.outbox.AggregateTypeVo;
import com.architecture.hexagonal.domain.model.vo.outbox.OutboxStatusVo;
import com.architecture.hexagonal.infrastructure.outbound.message.rabbitmq.mapper.user.PayloadMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class UserMessageSender {

  private final IUserEventsProducer userEventStreamPublisher;
  private final PayloadMapper payloadMapper;
  private final OutboxRepositoryWritePort outboxRepositoryWritePort;

  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  public void sendUserCreatedMessage(final UserCreated userCreated) {
    if (isSendUserCreatedMessage(userCreated)) {
      return;
    }

    outboxRepositoryWritePort.upsertByEventIdentity(
        Outbox.builder()
            .aggregateId(userCreated.getData().getUserId())
            .aggregateType(AggregateTypeVo.USER)
            .action(ActionType.UserActionType.USER_CREATED.getActionType())
            .status(OutboxStatusVo.PENDING)
            .payload(payloadMapper.toPayload(userCreated))
            .createdAt(userCreated.getMessageHeader().getMessageDate())
            .build());
  }

  private boolean isSendUserCreatedMessage(final UserCreated userCreated) {
    try {
      return userEventStreamPublisher.publishUserCreated(userCreated);
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
            .aggregateId(userUpdated.getData().getUserId())
            .aggregateType(AggregateTypeVo.USER)
            .action(ActionType.UserActionType.USER_UPDATED.getActionType())
            .status(OutboxStatusVo.PENDING)
            .payload(payloadMapper.toPayload(userUpdated))
            .createdAt(userUpdated.getMessageHeader().getMessageDate())
            .build());
  }

  private boolean isSendUserUpdatedMessage(final UserUpdated userUpdated) {
    try {
      return userEventStreamPublisher.publishUserUpdated(userUpdated);
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
            .aggregateId(userDeleted.getData().getUserId())
            .aggregateType(AggregateTypeVo.USER)
            .action(ActionType.UserActionType.USER_DELETED.getActionType())
            .status(OutboxStatusVo.PENDING)
            .payload(payloadMapper.toPayload(userDeleted))
            .createdAt(userDeleted.getMessageHeader().getMessageDate())
            .build());
  }

  private boolean isSendUserDeletedMessage(final UserDeleted userDeleted) {
    try {
      return userEventStreamPublisher.publishUserDeleted(userDeleted);
    } catch (Exception _) {
      return false;
    }
  }
}
