package com.architecture.hexagonal.infrastructure.outbound.message.sender;

import com.architecture.hexagonal.infrastructure.outbound.message.data.UserCreated;
import com.architecture.hexagonal.infrastructure.outbound.message.data.UserDeleted;
import com.architecture.hexagonal.infrastructure.outbound.message.data.UserUpdated;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class UserMessageSender {

  private final StreamBridge streamBridge;

  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  public void sendUserCreatedMessage(final UserCreated event) {
    streamBridge.send(UserMessageNaming.PUBLISH_USER_CREATED_OUT_BINDING, event);
  }

  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  public void sendUserUpdatedMessage(final UserUpdated event) {
    streamBridge.send(UserMessageNaming.PUBLISH_USER_UPDATED_OUT_BINDING, event);
  }

  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  public void sendUserDeletedMessage(final UserDeleted event) {
    streamBridge.send(UserMessageNaming.PUBLISH_USER_DELETED_OUT_BINDING, event);
  }
}
