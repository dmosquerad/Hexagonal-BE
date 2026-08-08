package com.architecture.hexagonal.application.technical.outbox.block.usecase.impl;

import com.architecture.hexagonal.application.port.database.OutboxRepositoryWritePort;
import com.architecture.hexagonal.application.technical.outbox.block.usecase.BlockOutboxEventUseCase;
import com.architecture.hexagonal.domain.model.entity.OutboxDo;
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
  public OutboxDo execute(final @NonNull OutboxDo outboxDo) {
    return outboxRepositoryWritePort.save(
        outboxDo.toBuilder()
            .status(OutboxStatusVo.BLOCKED)
            .processedAt(OffsetDateTime.now(clock))
            .build());
  }
}
