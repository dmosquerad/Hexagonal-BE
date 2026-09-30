package com.architecture.hexagonal.application.testutils.usecase.technical.outbox.find;

import com.architecture.hexagonal.application.usecase.technical.outbox.find.input.FindOutboxInput;
import com.architecture.hexagonal.domain.model.vo.outbox.AggregateTypeVo;
import com.architecture.hexagonal.domain.model.vo.outbox.OutboxStatusVo;
import lombok.Builder;

@Builder
public class FindOutboxInputTestDataBuilder {

    @Builder.Default private AggregateTypeVo aggregateType = AggregateTypeVo.USER;

    @Builder.Default private OutboxStatusVo status = OutboxStatusVo.PENDING;

    public FindOutboxInput findPendingOutboxEventsInput() {
        return FindOutboxInput.builder()
                .aggregateType(aggregateType)
                .status(status)
                .build();
    }
}
