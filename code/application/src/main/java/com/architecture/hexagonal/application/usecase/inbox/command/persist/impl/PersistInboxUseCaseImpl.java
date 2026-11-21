package com.architecture.hexagonal.application.usecase.inbox.command.persist.impl;

import com.architecture.hexagonal.application.service.inbox.InboxService;
import com.architecture.hexagonal.contract.bus.model.inbox.command.RegisterInboxCommand;
import com.architecture.hexagonal.domain.annotation.ApplicationUseCase;
import com.architecture.hexagonal.domain.model.entity.inbox.Inbox;
import com.architecture.hexagonal.domain.model.vo.date.DateVo;
import com.architecture.hexagonal.domain.model.vo.payload.AggregateTypeVo;
import com.architecture.hexagonal.domain.model.vo.payload.MessageHeaderVo;
import com.architecture.hexagonal.domain.model.vo.payload.PayloadVo;
import com.architecture.hexagonal.domain.model.vo.payload.StatusVo;
import com.architecture.hexagonal.domain.port.in.bus.command.CommandHandler;
import com.architecture.hexagonal.domain.port.in.transaction.TransactionWriteBoundary;
import java.time.OffsetDateTime;
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
                            .messageDate(toDateVo(command.getMessageHeader().getMessageDate()))
                            .build())
                    .data(command.getData())
                    .build())
            .aggregateType(AggregateTypeVo.valueOf(command.getAggregateType()))
            .aggregateId(command.getAggregateId())
            .action(command.getAction())
            .status(StatusVo.PENDING)
            .createdAt(toDateVo(command.getCreatedAt()))
            .build();

    return transactionWriteBoundary.execute(() -> inboxService.save(inbox));
  }

  private DateVo toDateVo(final OffsetDateTime offsetDateTime) {
    if (offsetDateTime == null) {
      return null;
    }

    return DateVo.builder()
        .date(offsetDateTime.toInstant())
        .zoneId(offsetDateTime.getOffset())
        .build();
  }
}
