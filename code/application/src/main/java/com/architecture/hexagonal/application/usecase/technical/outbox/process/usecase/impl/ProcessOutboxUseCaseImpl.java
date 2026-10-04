package com.architecture.hexagonal.application.usecase.technical.outbox.process.usecase.impl;

import com.architecture.hexagonal.application.port.configuration.SchedulerConfigurationPort;
import com.architecture.hexagonal.application.port.database.OutboxRepositoryWritePort;
import com.architecture.hexagonal.application.port.outbox.OutboxProcessPort;
import com.architecture.hexagonal.application.usecase.technical.outbox.process.input.ProcessOutboxInput;
import com.architecture.hexagonal.application.usecase.technical.outbox.process.usecase.ProcessOutboxUseCase;
import com.architecture.hexagonal.domain.model.entity.outbox.Outbox;
import com.architecture.hexagonal.domain.model.vo.outbox.OutboxStatusVo;
import java.time.Clock;
import java.time.OffsetDateTime;
import java.util.Optional;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ProcessOutboxUseCaseImpl implements ProcessOutboxUseCase {

  private final OutboxRepositoryWritePort outboxRepositoryWritePort;
  private final OutboxProcessPort outboxProcessPort;
  private final SchedulerConfigurationPort schedulerConfigurationPort;
  private final Clock clock;

  @Override
  public Outbox execute(final @NonNull ProcessOutboxInput processOutboxInput) {
    final Outbox outbox = processOutboxInput.outbox();
    final Outbox processingOutbox =
        outbox.toBuilder()
            .status(OutboxStatusVo.PROCESSING)
            .retryCount(outbox.retryCount() + 1)
            .processedAt(OffsetDateTime.now(clock))
            .build();

    final Optional<Outbox> claimedOutbox =
        outboxRepositoryWritePort.claimPendingForProcessing(processingOutbox);
    if (claimedOutbox.isEmpty()) {
      return outbox;
    }

    if (outbox.retryCount() >= schedulerConfigurationPort.getOutboxScheduler().maxRetries()) {
      return outboxRepositoryWritePort.upsertByEventIdentity(
          claimedOutbox.get().toBuilder()
              .status(OutboxStatusVo.FAILED)
              .processedAt(OffsetDateTime.now(clock))
              .build());
    }

    try {
      outboxProcessPort.process(claimedOutbox.get());
      return outboxRepositoryWritePort.upsertByEventIdentity(
          claimedOutbox.get().toBuilder().status(OutboxStatusVo.PUBLISHED).build());
    } catch (Exception _) {
      return outboxRepositoryWritePort.upsertByEventIdentity(
          claimedOutbox.get().toBuilder().status(OutboxStatusVo.PENDING).build());
    }
  }
}
