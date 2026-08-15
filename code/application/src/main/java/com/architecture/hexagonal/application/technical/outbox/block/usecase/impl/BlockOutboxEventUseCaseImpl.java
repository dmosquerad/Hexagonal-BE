package com.architecture.hexagonal.application.technical.outbox.block.usecase.impl;

import com.architecture.hexagonal.application.port.database.OutboxRepositoryWritePort;
import com.architecture.hexagonal.application.technical.outbox.block.usecase.BlockOutboxEventUseCase;
import com.architecture.hexagonal.domain.model.aggregate.outbox.Outbox;
import com.architecture.hexagonal.domain.model.vo.OutboxStatusVo;
import java.time.Clock;
import java.time.OffsetDateTime;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class BlockOutboxEventUseCaseImpl implements BlockOutboxEventUseCase {

  private final OutboxRepositoryWritePort outboxRepositoryWritePort;
  private final Clock clock;

  @Override
  public Outbox execute(final @NonNull Outbox outbox) {
    return outboxRepositoryWritePort.save(
        outbox.toBuilder()
            .status(OutboxStatusVo.BLOCKED)
            .processedAt(OffsetDateTime.now(clock))
            .build());
  }
}
