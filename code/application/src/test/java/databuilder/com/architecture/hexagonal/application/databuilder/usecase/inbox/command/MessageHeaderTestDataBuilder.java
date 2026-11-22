package com.architecture.hexagonal.application.databuilder.usecase.inbox.command;

import com.architecture.hexagonal.application.databuilder.usecase.common.DateTestDataBuilder;
import com.architecture.hexagonal.contract.bus.model.common.Date;
import com.architecture.hexagonal.contract.bus.model.inbox.command.MessageHeader;
import java.util.UUID;
import lombok.Builder;

@Builder
public class MessageHeaderTestDataBuilder {

  @Builder.Default private UUID messageId = UUID.fromString("4059510b-ceb3-4d4c-913e-1759acbd63b1");

  @Builder.Default private Date messageDate = DateTestDataBuilder.builder().build().date();

  public MessageHeader messageHeader() {
    final MessageHeader messageHeader = new MessageHeader();
    messageHeader.setMessageId(messageId);
    messageHeader.setMessageDate(messageDate);
    return messageHeader;
  }
}
