package com.architecture.hexagonal.application.port.database;

import com.architecture.hexagonal.domain.model.aggregate.outbox.Outbox;
import lombok.NonNull;

public interface OutboxRepositoryWritePort {
  Outbox save(@NonNull Outbox outbox);
}
