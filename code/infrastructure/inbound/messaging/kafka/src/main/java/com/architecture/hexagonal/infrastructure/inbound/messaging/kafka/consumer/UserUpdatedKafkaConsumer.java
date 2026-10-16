package com.architecture.hexagonal.infrastructure.inbound.messaging.kafka.consumer;

import com.architecture.hexagonal.contract.bus.model.inbox.command.RegisterInboxCommand;
import com.architecture.hexagonal.contract.inbound.messaging.kafka.client.consumer.UserUpdatedConsumer;
import com.architecture.hexagonal.contract.inbound.messaging.kafka.model.UserUpdatedPayload;
import com.architecture.hexagonal.infrastructure.bus.command.CommandBus;
import com.architecture.hexagonal.infrastructure.inbound.messaging.kafka.mapper.UserKafkaMessageMapper;
import java.time.Clock;
import java.time.OffsetDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserUpdatedKafkaConsumer implements UserUpdatedConsumer {

  private final CommandBus commandBus;
  private final UserKafkaMessageMapper userKafkaMessageMapper;
  private final Clock clock;

  @Override
  @KafkaListener(topics = UserUpdatedConsumer.USER_UPDATED_TOPIC_ADDRESS)
  public void listenUserUpdated(
      final UserUpdatedPayload userUpdatedMessage,
      final String receivedTopic,
      final String typeId) {
    final OffsetDateTime currentDateTime = OffsetDateTime.now(clock);

    try {
      commandBus.execute(userKafkaMessageMapper.toUpdateUserCommand(userUpdatedMessage));
    } catch (final Exception _) {
      final RegisterInboxCommand registerInboxCommand =
          userKafkaMessageMapper.toRegisterInboxCommand(userUpdatedMessage);
      registerInboxCommand.setCreatedAt(currentDateTime);

      commandBus.execute(registerInboxCommand);
    }
  }
}
