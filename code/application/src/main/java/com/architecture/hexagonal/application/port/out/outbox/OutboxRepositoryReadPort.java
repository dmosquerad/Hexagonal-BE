package com.architecture.hexagonal.application.port.out.outbox;

import com.architecture.hexagonal.application.projector.outbox.OutboxStatusAndAggregateProjector;
import com.architecture.hexagonal.domain.model.entity.outbox.Outbox;
import java.util.List;

public interface OutboxRepositoryReadPort {
  List<Outbox> findByStatusAndAggregateType(OutboxStatusAndAggregateProjector projector);
}
