package com.architecture.hexagonal.infrastructure.inbound.orchestration.orchestrator.outbox.retry;

import com.architecture.hexagonal.application.technical.outbox.block.usecase.BlockOutboxEventUseCase;
import com.architecture.hexagonal.application.technical.outbox.find.usecase.FindPendingOutboxEventsUseCase;
import com.architecture.hexagonal.application.technical.outbox.process.usecase.ProcessOutboxEventUseCase;
import com.architecture.hexagonal.domain.model.aggregate.outbox.Outbox;
import com.architecture.hexagonal.infrastructure.inbound.contract.orchestration.generated.retry.RetryOutboxeventCommandDto;
import com.architecture.hexagonal.infrastructure.inbound.orchestration.config.transaction.TransactionBoundary;
import com.architecture.hexagonal.infrastructure.inbound.orchestration.dispatcher.command.CommandHandler;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RetryOutboxEventsCommandHandlerImpl
    implements CommandHandler<RetryOutboxeventCommandDto, Void> {

  private final TransactionBoundary transactionBoundary;
  private final FindPendingOutboxEventsUseCase findPendingOutboxEventsUseCase;
  private final BlockOutboxEventUseCase blockOutboxEventUseCase;
  private final ProcessOutboxEventUseCase processOutboxEventUseCase;

  @Override
  public Void handle(final @NonNull RetryOutboxeventCommandDto retryOutboxeventCommandDto) {
    final List<Outbox> pendingEvents =
        transactionBoundary.read(findPendingOutboxEventsUseCase::execute);
    final Set<String> seenAggregateIds = Collections.synchronizedSet(new HashSet<>());

    for (final Outbox outbox : pendingEvents) {
      final Function<Outbox, Outbox> handler =
          seenAggregateIds.add(outbox.aggregateId())
              ? processOutboxEventUseCase::execute
              : blockOutboxEventUseCase::execute;

      transactionBoundary.write(() -> handler.apply(outbox));
    }

    return null;
  }
}
