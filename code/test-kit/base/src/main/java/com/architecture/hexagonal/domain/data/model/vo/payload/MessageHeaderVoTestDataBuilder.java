package com.architecture.hexagonal.domain.data.model.vo.payload;

import com.architecture.hexagonal.domain.data.time.TestClock;
import com.architecture.hexagonal.domain.model.vo.payload.MessageHeaderVo;
import java.time.OffsetDateTime;
import java.util.UUID;
import lombok.Builder;

@Builder
public class MessageHeaderVoTestDataBuilder {

  @Builder.Default private UUID messageId = UUID.fromString("4059510b-ceb3-4d4c-913e-1759acbd63b1");

  @Builder.Default private OffsetDateTime messageDate = OffsetDateTime.now(TestClock.FIXED_CLOCK);

  public MessageHeaderVo messageHeaderVo() {
    return MessageHeaderVo.builder().messageId(messageId).messageDate(messageDate).build();
  }
}
