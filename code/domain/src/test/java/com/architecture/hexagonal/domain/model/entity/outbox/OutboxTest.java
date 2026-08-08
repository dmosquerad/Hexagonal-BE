package com.architecture.hexagonal.domain.model.entity.outbox;

import com.architecture.hexagonal.domain.testutils.data.model.entity.outbox.OutboxTestDataBuilder;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class OutboxTest {

  @Test
  void getId_shouldReturnOutboxId() {
    final Outbox outbox = OutboxTestDataBuilder.builder().build().outbox();

    Assertions.assertThat(outbox.getId())
        .isEqualTo(
            Outbox.OutboxKey.builder()
                .aggregateType(outbox.aggregateType())
                .aggregateId(outbox.aggregateId())
                .action(outbox.action())
                .messageHeaderVo(outbox.payload().messageHeader())
                .build());
  }
}
