package com.architecture.hexagonal.application.service.technical.inbox.impl;

import com.architecture.hexagonal.application.annotation.ApplicationService;
import com.architecture.hexagonal.application.port.out.inbox.InboxProcess;
import com.architecture.hexagonal.application.port.out.inbox.InboxRepositoryReadPort;
import com.architecture.hexagonal.application.port.out.inbox.InboxRepositoryWritePort;
import com.architecture.hexagonal.application.port.out.scheduler.SchedulerConfigurationPort;
import com.architecture.hexagonal.application.service.technical.inbox.InboxService;
import com.architecture.hexagonal.domain.model.entity.inbox.Inbox;
import com.architecture.hexagonal.domain.model.vo.payload.AggregateTypeVo;
import com.architecture.hexagonal.domain.model.vo.payload.StatusVo;
import java.time.Clock;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@ApplicationService
@RequiredArgsConstructor
public class InboxServiceImpl implements InboxService {
  private final InboxRepositoryReadPort inboxRepositoryReadPort;
  private final InboxRepositoryWritePort inboxRepositoryWritePort;
  private final InboxProcess inboxProcess;
  private final SchedulerConfigurationPort schedulerConfigurationPort;
  private final Clock clock;

  @Override
  public Inbox save(final @NonNull Inbox inbox) {
    return inboxRepositoryWritePort.save(inbox);
  }

  @Override
  public List<Inbox> findPending(final @NonNull AggregateTypeVo aggregateType) {
    return inboxRepositoryReadPort.findByStatusAndAggregateType(StatusVo.PENDING, aggregateType);
  }

  @Override
  public Inbox process(final @NonNull Inbox inbox) {
    final Inbox processingInbox =
        inbox.toBuilder()
            .status(StatusVo.PROCESSING)
            .retryCount(inbox.retryCount() + 1)
            .processedAt(OffsetDateTime.now(clock))
            .build();

    final Optional<Inbox> claimedInbox =
        inboxRepositoryWritePort.claimPendingForProcessing(processingInbox);
    if (claimedInbox.isEmpty()) {
      return inbox;
    }

    if (claimedInbox.get().retryCount()
        >= schedulerConfigurationPort.getInboxScheduler().maxRetries()) {
      return inboxRepositoryWritePort.upsertByMessageIdentity(
          claimedInbox.get().toBuilder()
              .status(StatusVo.FAILED)
              .processedAt(OffsetDateTime.now(clock))
              .build());
    }

    try {
      inboxProcess.process(claimedInbox.get());
      return inboxRepositoryWritePort.upsertByMessageIdentity(
          claimedInbox.get().toBuilder().status(StatusVo.PROCESSED).build());
    } catch (Exception _) {
      return inboxRepositoryWritePort.upsertByMessageIdentity(
          claimedInbox.get().toBuilder().status(StatusVo.PENDING).build());
    }
  }
}
