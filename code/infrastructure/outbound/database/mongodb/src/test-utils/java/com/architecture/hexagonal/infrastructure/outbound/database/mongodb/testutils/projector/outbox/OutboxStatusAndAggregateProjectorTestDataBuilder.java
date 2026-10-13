package com.architecture.hexagonal.infrastructure.outbound.database.mongodb.testutils.projector.outbox;

import com.architecture.hexagonal.application.projector.technical.outbox.OutboxStatusAndAggregateProjector;
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
