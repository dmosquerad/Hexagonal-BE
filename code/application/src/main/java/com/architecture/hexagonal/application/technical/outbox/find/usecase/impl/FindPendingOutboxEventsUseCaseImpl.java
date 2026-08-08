package com.architecture.hexagonal.application.technical.outbox.find.usecase.impl;

import com.architecture.hexagonal.application.port.database.OutboxRepositoryReadPort;
import com.architecture.hexagonal.application.technical.outbox.find.usecase.FindPendingOutboxEventsUseCase;
import com.architecture.hexagonal.domain.model.entity.OutboxDo;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class FindPendingOutboxEventsUseCaseImpl implements FindPendingOutboxEventsUseCase {

  private final OutboxRepositoryReadPort outboxRepositoryReadPort;

  @Override
  public List<OutboxDo> execute() {
    return outboxRepositoryReadPort.findPendingEvents();
  }
}
