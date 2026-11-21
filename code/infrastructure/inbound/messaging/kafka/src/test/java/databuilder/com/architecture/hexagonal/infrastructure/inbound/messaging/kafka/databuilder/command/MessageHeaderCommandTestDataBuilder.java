package com.architecture.hexagonal.infrastructure.inbound.messaging.kafka.databuilder.command;

import com.architecture.hexagonal.application.databuilder.model.common.DateTestDataBuilder;
import com.architecture.hexagonal.contract.bus.model.common.Date;
import com.architecture.hexagonal.contract.bus.model.inbox.command.MessageHeader;
import java.util.UUID;
import lombok.Builder;

@Builder
public class MessageHeaderCommandTestDataBuilder {

  @Builder.Default private final UUID messageId = UUID.fromString("4059510b-ceb3-4d4c-913e-1759acbd63b1");

  @Builder.Default private Date date = DateTestDataBuilder.builder().build().date();

  public MessageHeader messageHeader() {
    final MessageHeader header = new MessageHeader();
    header.setMessageId(messageId);
    header.setMessageDate(date);
    return header;
  }
}
