package com.architecture.hexagonal.application.projector.outbox;

import com.architecture.hexagonal.domain.model.vo.payload.AggregateTypeVo;
import com.architecture.hexagonal.domain.model.vo.payload.StatusVo;
import lombok.Builder;

@Builder
public record OutboxStatusAndAggregateProjector(AggregateTypeVo aggregateType, StatusVo status) {}
