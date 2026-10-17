package com.architecture.hexagonal.application.usecase.inbox.retry.impl;

import com.architecture.hexagonal.application.annotation.ApplicationUseCase;
import com.architecture.hexagonal.application.port.in.bus.command.CommandHandler;
import com.architecture.hexagonal.application.port.out.transaction.TransactionReadBoundary;
import com.architecture.hexagonal.application.service.inbox.InboxService;
import com.architecture.hexagonal.contract.application.usecase.generated.retry.RetryInboxeventCommand;
import com.architecture.hexagonal.domain.model.entity.inbox.Inbox;
import com.architecture.hexagonal.domain.model.vo.payload.AggregateTypeVo;
import java.util.List;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@ApplicationUseCase
@RequiredArgsConstructor
public class RetryInboxUseCaseImpl implements CommandHandler<RetryInboxeventCommand, Void> {
  private final TransactionReadBoundary transactionReadBoundary;
  private final InboxService inboxService;

  @Override
  public Void handle(final @NonNull RetryInboxeventCommand command) {
    for (final AggregateTypeVo aggregateType : AggregateTypeVo.values()) {
      final List<Inbox> pendingEvents =
          transactionReadBoundary.execute(() -> inboxService.findPending(aggregateType));
      for (final Inbox inbox : pendingEvents) {
        inboxService.process(inbox);
      }
    }
    return null;
  }
}
