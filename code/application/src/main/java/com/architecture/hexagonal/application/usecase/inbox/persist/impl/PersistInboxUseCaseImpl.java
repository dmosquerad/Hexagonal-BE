package com.architecture.hexagonal.application.usecase.inbox.persist.impl;

import com.architecture.hexagonal.application.annotation.ApplicationUseCase;
import com.architecture.hexagonal.application.port.in.bus.command.CommandHandler;
import com.architecture.hexagonal.application.port.out.transaction.TransactionWriteBoundary;
import com.architecture.hexagonal.application.service.technical.inbox.InboxService;
import com.architecture.hexagonal.contract.application.usecase.generated.inbox.RegisterInboxCommand;
import com.architecture.hexagonal.domain.model.entity.inbox.Inbox;
import com.architecture.hexagonal.domain.model.vo.payload.AggregateTypeVo;
import com.architecture.hexagonal.domain.model.vo.payload.MessageHeaderVo;
import com.architecture.hexagonal.domain.model.vo.payload.PayloadVo;
import com.architecture.hexagonal.domain.model.vo.payload.StatusVo;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@ApplicationUseCase
@RequiredArgsConstructor
public class PersistInboxUseCaseImpl implements CommandHandler<RegisterInboxCommand, Inbox> {
  private final TransactionWriteBoundary transactionWriteBoundary;
  private final InboxService inboxService;

  @Override
  public Inbox handle(final @NonNull RegisterInboxCommand command) {
    final Inbox inbox =
        Inbox.builder()
            .payload(
                PayloadVo.builder()
                    .messageHeader(
                        MessageHeaderVo.builder()
                            .messageId(command.getMessageHeader().getMessageId())
                            .messageDate(command.getMessageHeader().getMessageDate())
                            .build())
                    .data(command.getData())
                    .build())
            .aggregateType(AggregateTypeVo.valueOf(command.getAggregateType()))
            .aggregateId(command.getAggregateId())
            .action(command.getAction())
            .status(StatusVo.PENDING)
            .createdAt(command.getCreatedAt())
            .build();

    return transactionWriteBoundary.execute(() -> inboxService.save(inbox));
  }
}
