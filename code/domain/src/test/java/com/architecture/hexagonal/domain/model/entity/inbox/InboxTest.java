package com.architecture.hexagonal.domain.model.entity.inbox;

import com.architecture.hexagonal.domain.testutils.data.model.entity.inbox.InboxTestDataBuilder;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class InboxTest {

  @Test
  void getIdShouldReturnMessageIdentity() {
    final Inbox inbox = InboxTestDataBuilder.builder().build().inbox();

    Assertions.assertThat(inbox.getId())
        .isEqualTo(
            Inbox.InboxKey.builder()
                .messageId(inbox.payload().messageHeader().messageId())
                .messageDate(inbox.payload().messageHeader().messageDate())
                .build());
  }
}
