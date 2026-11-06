package com.architecture.hexagonal.application.usecase.inbox.command.persist.impl;

import com.architecture.hexagonal.application.port.out.transaction.TransactionWriteBoundary;
import com.architecture.hexagonal.application.service.inbox.InboxService;
import com.architecture.hexagonal.application.testutils.transaction.SpyTransactionWriteBoundary;
import com.architecture.hexagonal.application.testutils.usecase.inbox.command.RegisterInboxInputTestDataBuilder;
import com.architecture.hexagonal.contract.bus.model.inbox.command.RegisterInboxCommand;
import com.architecture.hexagonal.domain.model.entity.inbox.Inbox;
import java.util.function.Supplier;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.AdditionalAnswers;
import org.mockito.ArgumentCaptor;
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
    final RegisterInboxCommand command =
        RegisterInboxInputTestDataBuilder.builder().build().registerInboxInput();

    Mockito.when(inboxService.save(Mockito.any(Inbox.class)))
        .thenAnswer(AdditionalAnswers.returnsFirstArg());

    final Inbox result = useCase.handle(command);

    final ArgumentCaptor<Inbox> inboxCaptor = ArgumentCaptor.forClass(Inbox.class);
    Mockito.verify(inboxService).save(inboxCaptor.capture());
    final Inbox savedInbox = inboxCaptor.getValue();

    AssertionsForClassTypes.assertThat(result).isSameAs(savedInbox);
    Mockito.verify(transactionWriteBoundary).execute(Mockito.<Supplier<Inbox>>any());
  }
}
