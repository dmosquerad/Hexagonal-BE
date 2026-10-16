package com.architecture.hexagonal.infrastructure.inbound.messaging.kafka.consumer;

import com.architecture.hexagonal.contract.bus.model.inbox.command.RegisterInboxCommand;
import com.architecture.hexagonal.contract.inbound.messaging.kafka.client.consumer.UserDeletedConsumer;
import com.architecture.hexagonal.contract.inbound.messaging.kafka.model.UserDeletedPayload;
import com.architecture.hexagonal.infrastructure.bus.command.CommandBus;
import com.architecture.hexagonal.infrastructure.inbound.messaging.kafka.mapper.UserKafkaMessageMapper;
import java.time.Clock;
import java.time.OffsetDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserDeletedKafkaConsumer implements UserDeletedConsumer {

  private final CommandBus commandBus;
  private final UserKafkaMessageMapper userKafkaMessageMapper;
  private final Clock clock;

  @Override
  @KafkaListener(topics = UserDeletedConsumer.USER_DELETED_TOPIC_ADDRESS)
  public void listenUserDeleted(
      final UserDeletedPayload userDeletedMessage,
      final String receivedTopic,
      final String typeId) {
    final OffsetDateTime currentDateTime = OffsetDateTime.now(clock);

    try {
      commandBus.execute(userKafkaMessageMapper.toDeleteUserCommand(userDeletedMessage));
    } catch (final Exception _) {
      final RegisterInboxCommand registerInboxCommand =
          userKafkaMessageMapper.toRegisterInboxCommand(userDeletedMessage);
      registerInboxCommand.setCreatedAt(currentDateTime);

      commandBus.execute(registerInboxCommand);
    }
  }
}
