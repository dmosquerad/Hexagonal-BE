package com.architecture.hexagonal.domain.port.out.repository.outbox;

import com.architecture.hexagonal.domain.model.criteria.outbox.OutboxStatusAndAggregateCriteria;
import com.architecture.hexagonal.domain.model.entity.outbox.Outbox;
import java.util.List;

public interface OutboxRepositoryReadPort {
  List<Outbox> findByStatusAndAggregateType(OutboxStatusAndAggregateCriteria criteria);
}
