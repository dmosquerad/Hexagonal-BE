package com.architecture.hexagonal.application.port.database;

import com.architecture.hexagonal.domain.model.entity.outbox.Outbox;
import java.util.Optional;
import lombok.NonNull;

public interface OutboxRepositoryWritePort {
  Outbox upsertByEventIdentity(@NonNull Outbox outbox);

  Optional<Outbox> claimPendingForProcessing(@NonNull Outbox outbox);
}
