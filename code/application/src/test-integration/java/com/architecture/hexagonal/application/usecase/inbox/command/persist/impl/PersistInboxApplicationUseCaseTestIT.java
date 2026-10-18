package com.architecture.hexagonal.application.usecase.inbox.command.persist.impl;

import com.architecture.hexagonal.application.port.out.inbox.InboxProcess;
import com.architecture.hexagonal.application.port.out.inbox.InboxRepositoryReadPort;
import com.architecture.hexagonal.application.port.out.inbox.InboxRepositoryWritePort;
import com.architecture.hexagonal.application.port.out.scheduler.SchedulerConfigurationPort;
import com.architecture.hexagonal.application.service.inbox.InboxService;
import com.architecture.hexagonal.application.service.inbox.impl.InboxServiceImpl;
import com.architecture.hexagonal.application.testutils.time.TestClock;
import com.architecture.hexagonal.application.testutils.transaction.SpyTransactionWriteBoundary;
import com.architecture.hexagonal.application.testutils.usecase.inbox.command.RegisterInboxInputTestDataBuilder;
import com.architecture.hexagonal.contract.bus.model.inbox.command.RegisterInboxCommand;
import com.architecture.hexagonal.domain.model.entity.inbox.Inbox;
import com.architecture.hexagonal.domain.model.vo.payload.AggregateTypeVo;
import com.architecture.hexagonal.domain.model.vo.payload.MessageHeaderVo;
import com.architecture.hexagonal.domain.model.vo.payload.PayloadVo;
import com.architecture.hexagonal.domain.model.vo.payload.StatusVo;
import java.time.Clock;
import java.util.function.Supplier;
import org.assertj.core.api.Assertions;
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
class PersistInboxApplicationUseCaseTestIT {

  @Mock private InboxRepositoryReadPort inboxRepositoryReadPort;
  @Mock private InboxRepositoryWritePort inboxRepositoryWritePort;
  @Mock private InboxProcess inboxProcess;
  @Mock private SchedulerConfigurationPort schedulerConfigurationPort;
  @Mock private InboxService inboxService;

  @Spy private Clock clock = TestClock.FIXED_CLOCK;
  @Spy private SpyTransactionWriteBoundary transactionWriteBoundary =
      new SpyTransactionWriteBoundary();

  @InjectMocks private InboxServiceImpl inboxServiceImpl;
  @InjectMocks private PersistInboxUseCaseImpl persistInboxUseCase;

  @Test
  void handleShouldSaveInboxWithinWriteTransaction() {
    final RegisterInboxCommand command =
        RegisterInboxInputTestDataBuilder.builder().build().registerInboxInput();
    final Inbox expectedInbox =
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
    Mockito.when(inboxRepositoryWritePort.save(Mockito.any(Inbox.class)))
        .thenAnswer(AdditionalAnswers.returnsFirstArg());
    Mockito.when(inboxService.save(Mockito.any(Inbox.class)))
        .thenAnswer(
            AdditionalAnswers.answer((Inbox candidate) -> inboxServiceImpl.save(candidate)));

    final Inbox result = persistInboxUseCase.handle(command);

    final ArgumentCaptor<Inbox> inboxCaptor = ArgumentCaptor.forClass(Inbox.class);
    Mockito.verify(inboxService).save(inboxCaptor.capture());
    final Inbox savedInbox = inboxCaptor.getValue();
    Assertions.assertThat(result).isSameAs(savedInbox);
    Assertions.assertThat(savedInbox)
        .usingRecursiveComparison()
        .isEqualTo(expectedInbox);
    Mockito.verify(transactionWriteBoundary).execute(Mockito.<Supplier<Inbox>>any());
    Mockito.verify(inboxRepositoryWritePort).save(savedInbox);
  }
}
