package com.architecture.hexagonal.application.usecase.technical.outbox.find.usecase.impl;

import com.architecture.hexagonal.application.port.database.OutboxRepositoryReadPort;
import com.architecture.hexagonal.application.usecase.technical.outbox.find.input.FindOutboxInput;
import com.architecture.hexagonal.application.usecase.technical.outbox.find.projector.OutboxStatusAndAggregateProjector;
import com.architecture.hexagonal.application.usecase.technical.outbox.find.usecase.FindOutboxUseCase;
import com.architecture.hexagonal.domain.model.entity.outbox.Outbox;
import java.util.List;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class FindOutboxUseCaseImpl implements FindOutboxUseCase {

  private final OutboxRepositoryReadPort outboxRepositoryReadPort;

  @Override
  public List<Outbox> execute(final @NonNull FindOutboxInput findOutboxInput) {
    final OutboxStatusAndAggregateProjector outboxStatusAndAggregateProjector =
        OutboxStatusAndAggregateProjector.builder()
            .aggregateType(findOutboxInput.aggregateType())
            .status(findOutboxInput.status())
            .build();
    return outboxRepositoryReadPort.findByStatusAndAggregateType(outboxStatusAndAggregateProjector);
  }
}
