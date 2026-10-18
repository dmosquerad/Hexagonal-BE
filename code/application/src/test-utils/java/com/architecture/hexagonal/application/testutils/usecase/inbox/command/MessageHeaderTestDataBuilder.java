package com.architecture.hexagonal.application.testutils.usecase.inbox.command;

import com.architecture.hexagonal.contract.bus.model.inbox.command.MessageHeader;
import com.architecture.hexagonal.domain.testutils.data.time.TestClock;
import java.time.OffsetDateTime;
import java.util.UUID;
import lombok.Builder;

@Builder
public class MessageHeaderTestDataBuilder {

  @Builder.Default
  private UUID messageId = UUID.fromString("4059510b-ceb3-4d4c-913e-1759acbd63b1");

  @Builder.Default
  private OffsetDateTime messageDate = OffsetDateTime.now(TestClock.FIXED_CLOCK);

  public MessageHeader messageHeader() {
    final MessageHeader messageHeader = new MessageHeader();
    messageHeader.setMessageId(messageId);
    messageHeader.setMessageDate(messageDate);
    return messageHeader;
  }
}
