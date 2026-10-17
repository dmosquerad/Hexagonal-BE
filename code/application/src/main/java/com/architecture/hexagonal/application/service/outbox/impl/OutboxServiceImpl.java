package com.architecture.hexagonal.application.service.outbox.impl;

import com.architecture.hexagonal.application.annotation.ApplicationService;
import com.architecture.hexagonal.application.port.out.outbox.OutboxProcess;
import com.architecture.hexagonal.application.port.out.outbox.OutboxRepositoryReadPort;
import com.architecture.hexagonal.application.port.out.outbox.OutboxRepositoryWritePort;
import com.architecture.hexagonal.application.port.out.scheduler.SchedulerConfigurationPort;
import com.architecture.hexagonal.application.projector.outbox.OutboxStatusAndAggregateProjector;
import com.architecture.hexagonal.application.service.outbox.OutboxService;
import com.architecture.hexagonal.domain.model.entity.outbox.Outbox;
import com.architecture.hexagonal.domain.model.vo.payload.StatusVo;
import java.time.Clock;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@ApplicationService
@RequiredArgsConstructor
public class OutboxServiceImpl implements OutboxService {
  private final OutboxRepositoryReadPort outboxRepositoryReadPort;
  private final OutboxRepositoryWritePort outboxRepositoryWritePort;
  private final OutboxProcess outboxProcess;
  private final SchedulerConfigurationPort schedulerConfigurationPort;
  private final Clock clock;

  @Override
  public List<Outbox> find(
      final @NonNull OutboxStatusAndAggregateProjector outboxStatusAndAggregateProjector) {
    return outboxRepositoryReadPort.findByStatusAndAggregateType(outboxStatusAndAggregateProjector);
  }

  @Override
  public Outbox process(final @NonNull Outbox outbox) {
    final Outbox processingOutbox =
        outbox.toBuilder()
            .status(StatusVo.PROCESSING)
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
              .status(StatusVo.FAILED)
              .processedAt(OffsetDateTime.now(clock))
              .build());
    }

    try {
      outboxProcess.process(claimedOutbox.get());
      return outboxRepositoryWritePort.upsertByEventIdentity(
          claimedOutbox.get().toBuilder().status(StatusVo.PROCESSED).build());
    } catch (Exception _) {
      return outboxRepositoryWritePort.upsertByEventIdentity(
          claimedOutbox.get().toBuilder().status(StatusVo.PENDING).build());
    }
  }
}
