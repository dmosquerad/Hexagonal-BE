package com.architecture.hexagonal.application.databuilder.model.vo.payload;

import com.architecture.hexagonal.application.databuilder.model.vo.time.DateTestDataBuilder;
import com.architecture.hexagonal.application.model.vo.payload.MessageHeader;
import com.architecture.hexagonal.application.model.vo.time.Date;
import java.util.UUID;
import lombok.Builder;

@Builder
public class MessageHeaderTestDataBuilder {

  @Builder.Default private UUID messageId = UUID.fromString("4059510b-ceb3-4d4c-913e-1759acbd63b1");

  @Builder.Default private Date messageDate = DateTestDataBuilder.builder().build().date();

  public MessageHeader messageHeader() {
    return MessageHeader.builder().messageId(messageId).messageDate(messageDate).build();
  }
}
