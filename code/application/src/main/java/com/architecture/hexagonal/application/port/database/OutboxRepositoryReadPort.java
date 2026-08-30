package com.architecture.hexagonal.application.port.database;

import com.architecture.hexagonal.domain.model.aggregate.outbox.Outbox;
import java.util.List;
import java.util.Optional;

public interface OutboxRepositoryReadPort {
  List<Outbox> findPendingEvents();

  Optional<Outbox> findByOutboxKey(Outbox.OutboxKey outboxKey);
}
