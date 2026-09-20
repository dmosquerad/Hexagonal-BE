package com.architecture.hexagonal.application.testutils.outbox.find;

import com.architecture.hexagonal.application.usecase.technical.outbox.find.projector.OutboxStatusAndAggregateProjector;
import com.architecture.hexagonal.domain.model.vo.outbox.AggregateTypeVo;
import com.architecture.hexagonal.domain.model.vo.outbox.OutboxStatusVo;
import lombok.Builder;

@Builder
public class OutboxStatusAndAggregateProjectorTestDataBuilder {

    @Builder.Default private AggregateTypeVo aggregateType = AggregateTypeVo.USER;

    @Builder.Default private OutboxStatusVo status = OutboxStatusVo.PENDING;

    public OutboxStatusAndAggregateProjector outboxStatusAndAggregateProjector() {
        return OutboxStatusAndAggregateProjector.builder()
                .aggregateType(aggregateType)
                .status(status)
                .build();
    }
}
