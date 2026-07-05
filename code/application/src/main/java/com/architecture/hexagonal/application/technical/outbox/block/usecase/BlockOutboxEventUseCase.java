package com.architecture.hexagonal.application.technical.outbox.block.usecase;

import com.architecture.hexagonal.domain.model.entity.OutboxDo;

public interface BlockOutboxEventUseCase {

  OutboxDo execute(OutboxDo outboxDo);
}
