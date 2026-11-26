package com.architecture.hexagonal.domain.databuilder.model.criteria.outbox;

import com.architecture.hexagonal.domain.model.criteria.outbox.OutboxStatusAndAggregateCriteria;
import com.architecture.hexagonal.domain.model.vo.payload.AggregateTypeVo;
import com.architecture.hexagonal.domain.model.vo.payload.StatusVo;
import lombok.Builder;

@Builder
public class OutboxStatusAndAggregateCriteriaTestDataBuilder {

  @Builder.Default private AggregateTypeVo aggregateType = AggregateTypeVo.USER;

  @Builder.Default private StatusVo status = StatusVo.PENDING;

  public OutboxStatusAndAggregateCriteria outboxStatusAndAggregateCriteria() {
    return OutboxStatusAndAggregateCriteria.builder()
        .aggregateType(aggregateType)
        .status(status)
        .build();
  }
}
