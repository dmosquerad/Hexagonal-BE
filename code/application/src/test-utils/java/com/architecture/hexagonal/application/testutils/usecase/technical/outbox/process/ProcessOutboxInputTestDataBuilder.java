package com.architecture.hexagonal.application.testutils.usecase.technical.outbox.process;

import com.architecture.hexagonal.application.usecase.technical.outbox.process.input.ProcessOutboxInput;
import com.architecture.hexagonal.application.testutils.model.aggregate.outbox.OutboxTestDataBuilder;
import com.architecture.hexagonal.domain.model.entity.outbox.Outbox;
import lombok.Builder;

@Builder
public class ProcessOutboxInputTestDataBuilder {

    @Builder.Default private Outbox outbox = OutboxTestDataBuilder.builder().build().outboxDo();

    public ProcessOutboxInput processOutboxInput() {
        return ProcessOutboxInput.builder()
                .outbox(outbox)
                .build();
    }

}
