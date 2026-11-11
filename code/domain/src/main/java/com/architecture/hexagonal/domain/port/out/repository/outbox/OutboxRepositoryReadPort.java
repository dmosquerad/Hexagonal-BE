package com.architecture.hexagonal.domain.port.out.repository.outbox;

import com.architecture.hexagonal.domain.model.entity.outbox.Outbox;
import com.architecture.hexagonal.domain.model.vo.projector.outbox.OutboxStatusAndAggregateProjector;
import java.util.List;

public interface OutboxRepositoryReadPort {
  List<Outbox> findByStatusAndAggregateType(OutboxStatusAndAggregateProjector projector);
}
