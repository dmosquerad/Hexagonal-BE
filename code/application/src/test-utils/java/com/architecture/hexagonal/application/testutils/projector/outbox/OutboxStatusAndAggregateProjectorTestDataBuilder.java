package com.architecture.hexagonal.application.testutils.projector.outbox;

import com.architecture.hexagonal.application.projector.outbox.OutboxStatusAndAggregateProjector;
import com.architecture.hexagonal.domain.model.vo.payload.AggregateTypeVo;
import com.architecture.hexagonal.domain.model.vo.payload.StatusVo;
import lombok.Builder;

@Builder
public class OutboxStatusAndAggregateProjectorTestDataBuilder {

  @Builder.Default
  private AggregateTypeVo aggregateType = AggregateTypeVo.USER;

  @Builder.Default
  private StatusVo status = StatusVo.PENDING;

  public OutboxStatusAndAggregateProjector outboxStatusAndAggregateProjector() {
    return OutboxStatusAndAggregateProjector.builder()
        .aggregateType(aggregateType)
        .status(status)
        .build();
  }
}
