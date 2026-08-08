package com.architecture.hexagonal.infrastructure.outbound.message.outbox.service;

import com.architecture.hexagonal.domain.model.entity.OutboxDo;
import lombok.NonNull;

public interface OutboxService {

  void process(@NonNull OutboxDo outboxDo);
}
