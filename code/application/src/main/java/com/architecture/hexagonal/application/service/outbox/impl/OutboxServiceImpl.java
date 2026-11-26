package com.architecture.hexagonal.application.service.outbox.impl;

import com.architecture.hexagonal.application.service.outbox.OutboxService;
import com.architecture.hexagonal.domain.annotation.ApplicationService;
import com.architecture.hexagonal.domain.model.criteria.outbox.OutboxStatusAndAggregateCriteria;
import com.architecture.hexagonal.domain.model.entity.outbox.Outbox;
import com.architecture.hexagonal.domain.model.vo.payload.StatusVo;
import com.architecture.hexagonal.domain.model.vo.time.factory.DateVoFactory;
import com.architecture.hexagonal.domain.port.out.configuration.SchedulerConfigurationPort;
import com.architecture.hexagonal.domain.port.out.outbox.OutboxProcess;
import com.architecture.hexagonal.domain.port.out.repository.outbox.OutboxRepositoryReadPort;
import com.architecture.hexagonal.domain.port.out.repository.outbox.OutboxRepositoryWritePort;
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

  @Override
  public List<Outbox> find(
      final @NonNull OutboxStatusAndAggregateCriteria outboxStatusAndAggregateCriteria) {
    return outboxRepositoryReadPort.findByStatusAndAggregateType(outboxStatusAndAggregateCriteria);
  }

  @Override
  public Outbox process(final @NonNull Outbox outbox) {
    final Outbox processingOutbox =
        outbox.toBuilder()
            .status(StatusVo.PROCESSING)
            .retryCount(outbox.retryCount() + 1)
            .processedAt(DateVoFactory.now())
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
              .processedAt(DateVoFactory.now())
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
