package com.architecture.hexagonal.application.port.database;

import com.architecture.hexagonal.application.usecase.technical.outbox.find.projector.OutboxStatusAndAggregateProjector;
import com.architecture.hexagonal.domain.model.entity.outbox.Outbox;
import java.util.List;

public interface OutboxRepositoryReadPort {
  List<Outbox> findByStatusAndAggregateType(
      OutboxStatusAndAggregateProjector outboxStatusAndAggregateProjector);
}
