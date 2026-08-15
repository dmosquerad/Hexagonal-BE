package com.architecture.hexagonal.application.technical.outbox.find.usecase.impl;

import com.architecture.hexagonal.application.port.database.OutboxRepositoryReadPort;
import com.architecture.hexagonal.application.technical.outbox.find.usecase.FindPendingOutboxEventsUseCase;
import com.architecture.hexagonal.domain.model.aggregate.outbox.Outbox;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class FindPendingOutboxEventsUseCaseImpl implements FindPendingOutboxEventsUseCase {

  private final OutboxRepositoryReadPort outboxRepositoryReadPort;

  @Override
  public List<Outbox> execute() {
    return outboxRepositoryReadPort.findPendingEvents();
  }
}
