package com.architecture.hexagonal.infrastructure.inbound.orchestration.orchestrator.outbox.retry;

import com.architecture.hexagonal.application.usecase.technical.outbox.find.input.FindOutboxInput;
import com.architecture.hexagonal.application.usecase.technical.outbox.find.usecase.FindOutboxUseCase;
import com.architecture.hexagonal.application.usecase.technical.outbox.process.input.ProcessOutboxInput;
import com.architecture.hexagonal.application.usecase.technical.outbox.process.usecase.ProcessOutboxUseCase;
import com.architecture.hexagonal.domain.model.entity.outbox.Outbox;
import com.architecture.hexagonal.domain.model.vo.outbox.AggregateTypeVo;
import com.architecture.hexagonal.domain.model.vo.outbox.OutboxStatusVo;
import com.architecture.hexagonal.infrastructure.inbound.contract.orchestration.generated.retry.RetryOutboxeventCommandDto;
import com.architecture.hexagonal.infrastructure.inbound.orchestration.config.transaction.TransactionBoundary;
import com.architecture.hexagonal.infrastructure.inbound.orchestration.dispatcher.command.CommandHandler;
import java.util.List;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RetryOutboxEventsCommandHandlerImpl
    implements CommandHandler<RetryOutboxeventCommandDto, Void> {

  private final TransactionBoundary transactionBoundary;
  private final FindOutboxUseCase findOutboxUseCase;
  private final ProcessOutboxUseCase processOutboxUseCase;

  @Override
  public Void handle(final @NonNull RetryOutboxeventCommandDto retryOutboxeventCommandDto) {

    for (AggregateTypeVo aggregateType : AggregateTypeVo.values()) {

      final List<Outbox> pendingEvents =
          transactionBoundary.read(
              () -> {
                final FindOutboxInput findOutboxInput =
                    FindOutboxInput.builder()
                        .aggregateType(aggregateType)
                        .status(OutboxStatusVo.PENDING)
                        .build();

                return findOutboxUseCase.execute(findOutboxInput);
              });

      for (final Outbox outbox : pendingEvents) {

        final ProcessOutboxInput processOutboxInput =
            ProcessOutboxInput.builder().outbox(outbox).build();

        transactionBoundary.write(() -> processOutboxUseCase.execute(processOutboxInput));
      }
    }
    return null;
  }
}
