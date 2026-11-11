package com.architecture.hexagonal.domain.data.model.vo.projector.outbox;

import com.architecture.hexagonal.domain.model.vo.payload.AggregateTypeVo;
import com.architecture.hexagonal.domain.model.vo.payload.StatusVo;
import com.architecture.hexagonal.domain.model.vo.projector.outbox.OutboxStatusAndAggregateProjector;
import lombok.Builder;

@Builder
public class OutboxStatusAndAggregateProjectorTestDataBuilder {

  @Builder.Default private AggregateTypeVo aggregateType = AggregateTypeVo.USER;

  @Builder.Default private StatusVo status = StatusVo.PENDING;

  public OutboxStatusAndAggregateProjector outboxStatusAndAggregateProjector() {
    return OutboxStatusAndAggregateProjector.builder()
        .aggregateType(aggregateType)
        .status(status)
        .build();
  }
}
