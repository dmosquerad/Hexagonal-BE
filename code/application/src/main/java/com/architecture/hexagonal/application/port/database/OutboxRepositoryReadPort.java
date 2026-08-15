package com.architecture.hexagonal.application.port.database;

import com.architecture.hexagonal.domain.model.aggregate.outbox.Outbox;
import java.util.List;

public interface OutboxRepositoryReadPort {
  List<Outbox> findPendingEvents();
}
