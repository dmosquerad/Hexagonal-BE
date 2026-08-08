package com.architecture.hexagonal.application.technical.outbox.process.usecase.impl;

import com.architecture.hexagonal.application.port.configuration.SchedulerConfigurationPort;
import com.architecture.hexagonal.application.port.database.OutboxRepositoryWritePort;
import com.architecture.hexagonal.application.port.outbox.OutboxProcessPort;
import com.architecture.hexagonal.application.technical.outbox.process.usecase.ProcessOutboxEventUseCase;
import com.architecture.hexagonal.domain.model.entity.OutboxDo;
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
  public OutboxDo execute(final @NonNull OutboxDo outboxDo) {
    if (outboxDo.getRetryCount()
        >= schedulerConfigurationPort.getSchedulerOutbox().getMaxRetries()) {
      return outboxRepositoryWritePort.save(
          outboxDo.toBuilder()
              .status(OutboxStatusVo.FAILED)
              .processedAt(OffsetDateTime.now(clock))
              .build());
    }

    return outboxRetry(outboxDo);
  }

  private OutboxDo outboxRetry(final OutboxDo outboxDo) {
    final OutboxDo processingOutboxDo =
        outboxDo.toBuilder()
            .status(OutboxStatusVo.PROCESSING)
            .retryCount(outboxDo.getRetryCount() + 1)
            .processedAt(OffsetDateTime.now(clock))
            .build();

    try {
      outboxRepositoryWritePort.save(processingOutboxDo);
      outboxProcessPort.process(processingOutboxDo);
      return outboxRepositoryWritePort.save(
          processingOutboxDo.toBuilder().status(OutboxStatusVo.PUBLISHED).build());
    } catch (Exception ex) {
      return outboxRepositoryWritePort.save(
          processingOutboxDo.toBuilder().status(OutboxStatusVo.PENDING).build());
    }
  }
}
