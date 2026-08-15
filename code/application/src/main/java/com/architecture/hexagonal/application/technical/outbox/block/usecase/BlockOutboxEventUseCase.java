package com.architecture.hexagonal.application.technical.outbox.block.usecase;

import com.architecture.hexagonal.domain.model.aggregate.outbox.Outbox;

public interface BlockOutboxEventUseCase {

  Outbox execute(Outbox outbox);
}
