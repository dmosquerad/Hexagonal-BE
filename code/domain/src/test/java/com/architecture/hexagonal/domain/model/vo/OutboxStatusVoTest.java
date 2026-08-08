package com.architecture.hexagonal.domain.model.vo;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class OutboxStatusVoTest {

  @Test
  void values_shouldContainAllStatuses() {
    final OutboxStatusVo[] values = OutboxStatusVo.values();

    Assertions.assertThat(values)
        .containsExactly(
            OutboxStatusVo.PENDING,
            OutboxStatusVo.PROCESSING,
            OutboxStatusVo.PUBLISHED,
            OutboxStatusVo.BLOCKED,
            OutboxStatusVo.FAILED,
            OutboxStatusVo.DEAD_LETTER);
  }
}
