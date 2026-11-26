package com.architecture.hexagonal.domain.model.criteria.outbox;

import com.architecture.hexagonal.domain.model.vo.payload.AggregateTypeVo;
import com.architecture.hexagonal.domain.model.vo.payload.StatusVo;
import lombok.Builder;

@Builder
public record OutboxStatusAndAggregateCriteria(AggregateTypeVo aggregateType, StatusVo status) {}
