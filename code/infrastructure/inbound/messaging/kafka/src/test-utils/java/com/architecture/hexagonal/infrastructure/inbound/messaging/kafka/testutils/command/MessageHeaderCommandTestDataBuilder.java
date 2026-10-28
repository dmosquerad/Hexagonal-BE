package com.architecture.hexagonal.infrastructure.inbound.messaging.kafka.testutils.command;

import com.architecture.hexagonal.contract.bus.model.inbox.command.MessageHeader;
import java.time.OffsetDateTime;
import java.util.UUID;
import lombok.Builder;

@Builder
public class MessageHeaderCommandTestDataBuilder {

  @Builder.Default
  private final UUID messageId = UUID.fromString("4059510b-ceb3-4d4c-913e-1759acbd63b1");

  @Builder.Default
  private final OffsetDateTime messageDate = OffsetDateTime.parse("2026-01-01T00:00:00Z");

  public MessageHeader messageHeader() {
    final MessageHeader header = new MessageHeader();
    header.setMessageId(messageId);
    header.setMessageDate(messageDate);
    return header;
  }
}
