package com.architecture.hexagonal.application.technical.outbox.process.usecase.impl;

import com.architecture.hexagonal.application.port.configuration.SchedulerConfigurationPort;
import com.architecture.hexagonal.application.port.database.OutboxRepositoryWritePort;
import com.architecture.hexagonal.application.port.outbox.OutboxProcessPort;
import com.architecture.hexagonal.application.technical.outbox.process.usecase.ProcessOutboxEventUseCase;
import com.architecture.hexagonal.domain.model.aggregate.outbox.Outbox;
import com.architecture.hexagonal.domain.model.vo.OutboxStatusVo;
import java.time.Clock;
import java.time.OffsetDateTime;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ProcessOutboxEventUseCaseImpl implements ProcessOutboxEventUseCase {

  private final OutboxRepositoryWritePort outboxRepositoryWritePort;
  private final OutboxProcessPort outboxProcessPort;
  private final SchedulerConfigurationPort schedulerConfigurationPort;
  private final Clock clock;

  @Override
  public Outbox execute(final @NonNull Outbox outbox) {
    if (outbox.retryCount() >= schedulerConfigurationPort.getSchedulerOutbox().maxRetries()) {
      return outboxRepositoryWritePort.save(
          outbox.toBuilder()
              .status(OutboxStatusVo.FAILED)
              .processedAt(OffsetDateTime.now(clock))
              .build());
    }

    return outboxRetry(outbox);
  }

  private Outbox outboxRetry(final Outbox outbox) {
    final Outbox processingOutbox =
        outbox.toBuilder()
            .status(OutboxStatusVo.PROCESSING)
            .retryCount(outbox.retryCount() + 1)
            .processedAt(OffsetDateTime.now(clock))
            .build();

    try {
      outboxRepositoryWritePort.save(processingOutbox);
      outboxProcessPort.process(processingOutbox);
      return outboxRepositoryWritePort.save(
          processingOutbox.toBuilder().status(OutboxStatusVo.PUBLISHED).build());
    } catch (Exception ex) {
      return outboxRepositoryWritePort.save(
          processingOutbox.toBuilder().status(OutboxStatusVo.PENDING).build());
    }
  }
}
