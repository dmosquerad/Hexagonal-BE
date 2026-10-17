package com.architecture.hexagonal.application.usecase.inbox.persist.impl;

import com.architecture.hexagonal.application.port.out.transaction.TransactionWriteBoundary;
import com.architecture.hexagonal.application.service.inbox.InboxService;
import com.architecture.hexagonal.application.testutils.transaction.SpyTransactionWriteBoundary;
import com.architecture.hexagonal.contract.bus.model.inbox.MessageHeader;
import com.architecture.hexagonal.contract.bus.model.inbox.RegisterInboxCommand;
import com.architecture.hexagonal.domain.model.entity.inbox.Inbox;
import java.time.OffsetDateTime;
import java.util.UUID;
import java.util.function.Supplier;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PersistInboxUseCaseImplTest {

  @Mock private InboxService inboxService;

  @Spy
  private TransactionWriteBoundary transactionWriteBoundary = new SpyTransactionWriteBoundary();

  @InjectMocks private PersistInboxUseCaseImpl useCase;

  @Test
  void handleShouldSaveInboxIfAbsentWithinWriteBoundary() {
    final Object data = new Object();
    final RegisterInboxCommand command = new RegisterInboxCommand();
    final MessageHeader messageHeader = new MessageHeader();
    messageHeader.setMessageId(UUID.fromString("b45f40b7-1b2b-4f4f-9f80-636bb1150c47"));
    messageHeader.setMessageDate(OffsetDateTime.parse("2026-10-13T11:00:00.123+02:00"));
    command.setMessageHeader(messageHeader);
    command.setData(data);
    command.setAggregateType("USER");
    command.setAction("USER_CREATED");
    command.setCreatedAt(OffsetDateTime.parse("2026-10-13T12:00:00.456+02:00"));
    Mockito.when(inboxService.save(Mockito.any()))
        .thenAnswer(invocation -> invocation.getArgument(0));

    final Inbox result = useCase.handle(command);

    org.assertj.core.api.Assertions.assertThat(result.aggregateType().name()).isEqualTo("USER");
    org.assertj.core.api.Assertions.assertThat(result.action()).isEqualTo("USER_CREATED");
    org.assertj.core.api.Assertions.assertThat(result.status().name()).isEqualTo("PENDING");
    org.assertj.core.api.Assertions.assertThat(result.retryCount()).isZero();
    org.assertj.core.api.Assertions.assertThat(result.createdAt().toString())
        .isEqualTo("2026-10-13T12:00:00.456+02:00");
    org.assertj.core.api.Assertions.assertThat(result.payload().messageHeader().messageId())
        .isEqualTo(messageHeader.getMessageId());
    org.assertj.core.api.Assertions.assertThat(
            result.payload().messageHeader().messageDate().toString())
        .isEqualTo("2026-10-13T11:00:00.123+02:00");
    org.assertj.core.api.Assertions.assertThat(result.payload().data()).isSameAs(data);
    Mockito.verify(inboxService).save(result);
    Mockito.verify(transactionWriteBoundary).execute(Mockito.<Supplier<Inbox>>any());
  }
}
