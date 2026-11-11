package com.architecture.hexagonal.application.service.outbox;

import com.architecture.hexagonal.domain.model.entity.outbox.Outbox;
import com.architecture.hexagonal.domain.model.vo.projector.outbox.OutboxStatusAndAggregateProjector;
import java.util.List;
import lombok.NonNull;

public interface OutboxService {
  List<Outbox> find(@NonNull OutboxStatusAndAggregateProjector outboxStatusAndAggregateProjector);

  Outbox process(@NonNull Outbox outbox);
}
