package com.architecture.hexagonal.application.technical.outbox.process.usecase;

import com.architecture.hexagonal.domain.model.entity.OutboxDo;

public interface ProcessOutboxEventUseCase {

  OutboxDo execute(OutboxDo outboxDo);
}
