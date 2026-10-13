package com.architecture.hexagonal.application.usecase.outbox.retry.impl;

import com.architecture.hexagonal.application.annotation.ApplicationUseCase;
import com.architecture.hexagonal.application.port.in.bus.command.CommandHandler;
import com.architecture.hexagonal.application.port.out.transaction.TransactionReadBoundary;
import com.architecture.hexagonal.application.port.out.transaction.TransactionWriteBoundary;
import com.architecture.hexagonal.application.projector.outbox.OutboxStatusAndAggregateProjector;
import com.architecture.hexagonal.application.service.technical.outbox.OutboxService;
import com.architecture.hexagonal.contract.application.usecase.generated.retry.RetryOutboxeventCommand;
import com.architecture.hexagonal.domain.model.entity.outbox.Outbox;
import com.architecture.hexagonal.domain.model.vo.outbox.AggregateTypeVo;
import com.architecture.hexagonal.domain.model.vo.outbox.OutboxStatusVo;
import java.util.List;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@ApplicationUseCase
@RequiredArgsConstructor
public class RetryOutboxUseCaseImpl implements CommandHandler<RetryOutboxeventCommand, Void> {
  private final TransactionReadBoundary transactionReadBoundary;
  private final TransactionWriteBoundary transactionWriteBoundary;
  private final OutboxService outboxService;

  @Override
  public Void handle(final @NonNull RetryOutboxeventCommand command) {
    for (final AggregateTypeVo aggregateType : AggregateTypeVo.values()) {
      final OutboxStatusAndAggregateProjector projector =
          OutboxStatusAndAggregateProjector.builder()
              .aggregateType(aggregateType)
              .status(OutboxStatusVo.PENDING)
              .build();
      final List<Outbox> pendingEvents =
          transactionReadBoundary.execute(() -> outboxService.find(projector));

      for (final Outbox outbox : pendingEvents) {
        transactionWriteBoundary.executeWithoutResult(() -> outboxService.process(outbox));
      }
    }
    return null;
  }
}
