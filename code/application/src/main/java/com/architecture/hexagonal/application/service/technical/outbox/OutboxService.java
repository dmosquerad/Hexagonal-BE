package com.architecture.hexagonal.application.service.technical.outbox;

import com.architecture.hexagonal.application.projector.outbox.OutboxStatusAndAggregateProjector;
import com.architecture.hexagonal.domain.model.entity.outbox.Outbox;
import java.util.List;
import lombok.NonNull;

public interface OutboxService {
  List<Outbox> find(@NonNull OutboxStatusAndAggregateProjector outboxStatusAndAggregateProjector);

  Outbox process(@NonNull Outbox outbox);
}
