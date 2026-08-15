package com.architecture.hexagonal.infrastructure.outbound.message.outbox.service;

import com.architecture.hexagonal.domain.model.aggregate.outbox.Outbox;
import lombok.NonNull;

public interface OutboxService {

  void process(@NonNull Outbox outbox);
}
