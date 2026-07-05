package com.architecture.hexagonal.infrastructure.outbound.message.outbox.service.impl;

import com.architecture.hexagonal.application.port.message.UserSenderPort;
import com.architecture.hexagonal.domain.model.entity.OutboxDo;
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
  public void process(final @NonNull OutboxDo outboxDo) {
    final Consumer<OutboxDo> handler = actionHandlers().get(outboxDo.getAction());
    if (Objects.isNull(handler)) {
      throw new IllegalArgumentException("Unsupported user action: " + outboxDo.getAction());
    }
    handler.accept(outboxDo);
  }

  private Map<String, Consumer<OutboxDo>> actionHandlers() {
    return Map.of(
        UserMessageNaming.ACTION_USER_CREATED, this::processUserCreated,
        UserMessageNaming.ACTION_USER_UPDATED, this::processUserUpdated,
        UserMessageNaming.ACTION_USER_DELETED, this::processUserDeleted);
  }

  private void processUserCreated(final @NonNull OutboxDo outboxDo) {
    final UserCreated userCreated = (UserCreated) outboxDo.getPayload();
    userSenderPort.userSenderCreated(userFromOutboxMapper.toUser(userCreated));
  }

  private void processUserUpdated(final @NonNull OutboxDo outboxDo) {
    final UserUpdated userUpdated = (UserUpdated) outboxDo.getPayload();
    userSenderPort.userSenderUpdated(userFromOutboxMapper.toUser(userUpdated));
  }

  private void processUserDeleted(final @NonNull OutboxDo outboxDo) {
    final UserDeleted userDeleted = (UserDeleted) outboxDo.getPayload();
    userSenderPort.userSenderDeleted(userFromOutboxMapper.toUser(userDeleted));
  }
}
