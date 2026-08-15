package com.architecture.hexagonal.infrastructure.outbound.message.outbox.service.impl;

import com.architecture.hexagonal.application.port.message.UserSenderPort;
import com.architecture.hexagonal.domain.model.aggregate.outbox.Outbox;
import com.architecture.hexagonal.infrastructure.outbound.message.outbox.mapper.user.UserFromOutboxMapper;
import com.architecture.hexagonal.infrastructure.outbound.message.outbox.service.OutboxService;
import com.architecture.hexagonal.infrastructure.outbound.message.rabbitmq.data.UserCreated;
import com.architecture.hexagonal.infrastructure.outbound.message.rabbitmq.data.UserDeleted;
import com.architecture.hexagonal.infrastructure.outbound.message.rabbitmq.data.UserUpdated;
import com.architecture.hexagonal.infrastructure.outbound.message.rabbitmq.naming.UserMessageNaming;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserOutboxServiceImpl implements OutboxService {

  private final UserSenderPort userSenderPort;
  private final UserFromOutboxMapper userFromOutboxMapper;

  @Override
  public void process(final @NonNull Outbox outbox) {
    final Consumer<Outbox> handler = actionHandlers().get(outbox.action());
    if (Objects.isNull(handler)) {
      throw new IllegalArgumentException("Unsupported user action: " + outbox.action());
    }
    handler.accept(outbox);
  }

  private Map<String, Consumer<Outbox>> actionHandlers() {
    return Map.of(
        UserMessageNaming.ACTION_USER_CREATED, this::processUserCreated,
        UserMessageNaming.ACTION_USER_UPDATED, this::processUserUpdated,
        UserMessageNaming.ACTION_USER_DELETED, this::processUserDeleted);
  }

  private void processUserCreated(final @NonNull Outbox outbox) {
    final UserCreated userCreated = (UserCreated) outbox.payload();
    userSenderPort.userSenderCreated(userFromOutboxMapper.toUser(userCreated));
  }

  private void processUserUpdated(final @NonNull Outbox outbox) {
    final UserUpdated userUpdated = (UserUpdated) outbox.payload();
    userSenderPort.userSenderUpdated(userFromOutboxMapper.toUser(userUpdated));
  }

  private void processUserDeleted(final @NonNull Outbox outbox) {
    final UserDeleted userDeleted = (UserDeleted) outbox.payload();
    userSenderPort.userSenderDeleted(userFromOutboxMapper.toUser(userDeleted));
  }
}
