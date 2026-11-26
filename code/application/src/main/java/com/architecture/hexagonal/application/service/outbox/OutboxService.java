package com.architecture.hexagonal.application.service.outbox;

import com.architecture.hexagonal.domain.model.criteria.outbox.OutboxStatusAndAggregateCriteria;
import com.architecture.hexagonal.domain.model.entity.outbox.Outbox;
import java.util.List;
import lombok.NonNull;

public interface OutboxService {
  List<Outbox> find(@NonNull OutboxStatusAndAggregateCriteria outboxStatusAndAggregateCriteria);

  Outbox process(@NonNull Outbox outbox);
}
