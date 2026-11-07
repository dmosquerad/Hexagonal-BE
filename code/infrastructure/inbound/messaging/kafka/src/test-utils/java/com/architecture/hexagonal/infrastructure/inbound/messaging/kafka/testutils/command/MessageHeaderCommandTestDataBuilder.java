package com.architecture.hexagonal.infrastructure.inbound.messaging.kafka.testutils.command;

import com.architecture.hexagonal.contract.bus.model.inbox.command.MessageHeader;
import com.architecture.hexagonal.domain.data.time.TestClock;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.UUID;
import lombok.Builder;

@Builder
public class MessageHeaderCommandTestDataBuilder {

  @Builder.Default
  private final UUID messageId = UUID.fromString("4059510b-ceb3-4d4c-913e-1759acbd63b1");

  @Builder.Default
  private final OffsetDateTime messageDate = TestClock.FIXED_INSTANT.atOffset(ZoneOffset.UTC);

  public MessageHeader messageHeader() {
    final MessageHeader header = new MessageHeader();
    header.setMessageId(messageId);
    header.setMessageDate(messageDate);
    return header;
  }
}
