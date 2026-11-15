package com.architecture.hexagonal.domain.databuilder.model.vo.payload;

import com.architecture.hexagonal.domain.databuilder.model.vo.date.DateVoTestDataBuilder;
import com.architecture.hexagonal.domain.model.vo.date.DateVo;
import com.architecture.hexagonal.domain.model.vo.payload.MessageHeaderVo;
import java.util.UUID;
import lombok.Builder;

@Builder
public class MessageHeaderVoTestDataBuilder {

  @Builder.Default private UUID messageId = UUID.fromString("4059510b-ceb3-4d4c-913e-1759acbd63b1");

  @Builder.Default private DateVo messageDate = DateVoTestDataBuilder.builder().build().dateVo();

  public MessageHeaderVo messageHeaderVo() {
    return MessageHeaderVo.builder().messageId(messageId).messageDate(messageDate).build();
  }
}
