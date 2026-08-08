package com.architecture.hexagonal.infrastructure.inbound.orchestration.testutils.data;

import com.architecture.hexagonal.application.usecase.technical.outbox.find.input.FindOutboxInput;
import com.architecture.hexagonal.domain.model.vo.outbox.AggregateTypeVo;
import com.architecture.hexagonal.domain.model.vo.outbox.OutboxStatusVo;
import lombok.Builder;

@Builder
public class FindPendingOutboxEventsInputTestDataBuilder {

    @Builder.Default private AggregateTypeVo aggregateType = AggregateTypeVo.USER;

    @Builder.Default private OutboxStatusVo status = OutboxStatusVo.PENDING;

    public FindOutboxInput findPendingOutboxEventsInput() {
        return FindOutboxInput.builder()
                .aggregateType(aggregateType)
                .status(status)
                .build();
    }
}
