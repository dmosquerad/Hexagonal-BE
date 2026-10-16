package com.architecture.hexagonal.domain.model.vo.payload;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class StatusVoTest {

  @Test
  void valuesShouldContainAllStatuses() {
    final StatusVo[] values = StatusVo.values();

    Assertions.assertThat(values)
        .containsExactly(
            StatusVo.PENDING,
            StatusVo.PROCESSING,
            StatusVo.PROCESSED,
            StatusVo.BLOCKED,
            StatusVo.FAILED,
            StatusVo.DEAD_LETTER);
  }
}
