package com.architecture.hexagonal.application.usecase.technical.outbox.find.projector;

import com.architecture.hexagonal.domain.model.vo.outbox.AggregateTypeVo;
import com.architecture.hexagonal.domain.model.vo.outbox.OutboxStatusVo;
import lombok.Builder;

@Builder
public record OutboxStatusAndAggregateProjector(
    AggregateTypeVo aggregateType, OutboxStatusVo status) {}
