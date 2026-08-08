package com.architecture.hexagonal.domain.model.vo.outbox;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class AggregateTypeVoTest {

  @Test
  void values_shouldContainAllAggregateTypes() {
    final AggregateTypeVo[] values = AggregateTypeVo.values();

    Assertions.assertThat(values).containsExactly(AggregateTypeVo.USER);
  }
}
