package com.architecture.hexagonal.domain.model.entity.inbox;

import static org.assertj.core.api.Assertions.assertThat;

import com.architecture.hexagonal.domain.model.vo.payload.MessageHeaderVo;
import com.architecture.hexagonal.domain.model.vo.payload.PayloadVo;
import java.time.OffsetDateTime;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class InboxTest {

  @Test
  void getIdShouldReturnMessageIdentity() {
    final UUID messageId = UUID.randomUUID();
    final OffsetDateTime messageDate = OffsetDateTime.parse("2026-10-13T10:15:30Z");
    final Inbox inbox =
        Inbox.builder()
            .payload(
                PayloadVo.builder()
                    .messageHeader(
                        MessageHeaderVo.builder()
                            .messageId(messageId)
                            .messageDate(messageDate)
                            .build())
                    .build())
            .build();

    assertThat(inbox.getId())
        .isEqualTo(Inbox.InboxKey.builder().messageId(messageId).messageDate(messageDate).build());
  }
}
