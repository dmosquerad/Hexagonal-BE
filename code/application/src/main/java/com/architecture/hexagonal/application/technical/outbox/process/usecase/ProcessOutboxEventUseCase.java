package com.architecture.hexagonal.application.technical.outbox.process.usecase;

import com.architecture.hexagonal.domain.model.aggregate.outbox.Outbox;

public interface ProcessOutboxEventUseCase {

  Outbox execute(Outbox outbox);
}
