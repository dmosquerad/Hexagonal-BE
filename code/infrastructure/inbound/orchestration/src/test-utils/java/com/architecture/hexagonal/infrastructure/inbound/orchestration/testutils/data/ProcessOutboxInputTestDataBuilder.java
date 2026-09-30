package com.architecture.hexagonal.infrastructure.inbound.orchestration.testutils.data;

import com.architecture.hexagonal.application.usecase.technical.outbox.process.input.ProcessOutboxInput;
import com.architecture.hexagonal.domain.model.entity.outbox.Outbox;
import lombok.Builder;

@Builder
public class ProcessOutboxInputTestDataBuilder {

    @Builder.Default private Outbox outbox = OutboxTestDataBuilder.builder().build().outbox();

    public ProcessOutboxInput processOutboxInput() {
        return ProcessOutboxInput.builder()
                .outbox(outbox)
                .build();
    }

}
