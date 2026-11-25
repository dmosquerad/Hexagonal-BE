package com.architecture.hexagonal.application.service.outbox;

import com.architecture.hexagonal.application.service.outbox.impl.OutboxServiceImpl;
import com.architecture.hexagonal.domain.databuilder.model.entity.outbox.OutboxTestDataBuilder;
import com.architecture.hexagonal.domain.databuilder.model.vo.outbox.SchedulerOutboxConfigurationVoTestDataBuilder;
import com.architecture.hexagonal.domain.databuilder.model.vo.time.TestTime;
import com.architecture.hexagonal.domain.model.entity.outbox.Outbox;
import com.architecture.hexagonal.domain.model.projector.outbox.OutboxStatusAndAggregateProjector;
import com.architecture.hexagonal.domain.model.vo.outbox.SchedulerOutboxConfigurationVo;
import com.architecture.hexagonal.domain.model.vo.payload.AggregateTypeVo;
import com.architecture.hexagonal.domain.model.vo.payload.StatusVo;
import com.architecture.hexagonal.domain.model.vo.time.factory.DateVoFactory;
import com.architecture.hexagonal.domain.port.out.configuration.SchedulerConfigurationPort;
import com.architecture.hexagonal.domain.port.out.outbox.OutboxProcess;
import com.architecture.hexagonal.domain.port.out.repository.outbox.OutboxRepositoryReadPort;
import com.architecture.hexagonal.domain.port.out.repository.outbox.OutboxRepositoryWritePort;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.assertj.core.api.AssertionsForClassTypes;
import org.assertj.core.api.AssertionsForInterfaceTypes;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.AdditionalAnswers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OutboxServiceImplTest {
  @Mock private OutboxRepositoryReadPort outboxRepositoryReadPort;

  @Mock private OutboxRepositoryWritePort outboxRepositoryWritePort;

  @Mock private OutboxProcess outboxProcess;

  @Mock private SchedulerConfigurationPort schedulerConfigurationPort;

  @InjectMocks private OutboxServiceImpl outboxService;

  private MockedStatic<Instant> instantMock;

  @BeforeEach
  void setUp() {
    instantMock = Mockito.mockStatic(Instant.class, Mockito.CALLS_REAL_METHODS);
  }

  @AfterEach
  void tearDown() {
    instantMock.close();
  }

  @Test
  void findShouldReturnPendingEvents() {
    final Outbox outbox = OutboxTestDataBuilder.builder().build().outbox();
    final OutboxStatusAndAggregateProjector projector =
        OutboxStatusAndAggregateProjector.builder()
            .aggregateType(AggregateTypeVo.USER)
            .status(StatusVo.PENDING)
            .build();

    Mockito.when(outboxRepositoryReadPort.findByStatusAndAggregateType(projector))
        .thenReturn(List.of(outbox));

    final List<Outbox> result = outboxService.find(projector);

    AssertionsForInterfaceTypes.assertThat(result).containsExactly(outbox);
    Mockito.verify(outboxRepositoryReadPort).findByStatusAndAggregateType(projector);
  }

  @Test
  void processShouldPublishEventWhenRetrySucceeds() {
    final Outbox outbox = OutboxTestDataBuilder.builder().build().outbox();
    final Outbox processing =
        outbox.toBuilder()
            .status(StatusVo.PROCESSING)
            .retryCount(1)
            .processedAt(DateVoFactory.from(TestTime.FIXED_INSTANT))
            .build();
    final Outbox published = processing.toBuilder().status(StatusVo.PROCESSED).build();
    final SchedulerOutboxConfigurationVo schedulerConfig =
        SchedulerOutboxConfigurationVoTestDataBuilder.builder()
            .build()
            .schedulerOutboxConfigurationVo();
    Mockito.when(schedulerConfigurationPort.getOutboxScheduler()).thenReturn(schedulerConfig);
    Mockito.when(outboxRepositoryWritePort.claimPendingForProcessing(Mockito.any(Outbox.class)))
        .thenReturn(Optional.of(processing));
    Mockito.when(outboxRepositoryWritePort.upsertByEventIdentity(Mockito.any(Outbox.class)))
        .thenAnswer(AdditionalAnswers.returnsFirstArg());

    instantMock.when(Instant::now).thenReturn(TestTime.FIXED_INSTANT);
    final Outbox result = outboxService.process(outbox);

    AssertionsForClassTypes.assertThat(result).usingRecursiveComparison().isEqualTo(published);
    Mockito.verify(schedulerConfigurationPort).getOutboxScheduler();
    Mockito.verify(outboxRepositoryWritePort).claimPendingForProcessing(processing);
    Mockito.verify(outboxProcess).process(processing);
    Mockito.verify(outboxRepositoryWritePort).upsertByEventIdentity(published);
  }

  @Test
  void processShouldNotPublishEventWhenClaimWasLost() {
    final Outbox outbox = OutboxTestDataBuilder.builder().build().outbox();
    Mockito.when(outboxRepositoryWritePort.claimPendingForProcessing(Mockito.any(Outbox.class)))
        .thenReturn(Optional.empty());

    instantMock.when(Instant::now).thenReturn(TestTime.FIXED_INSTANT);
    final Outbox result = outboxService.process(outbox);

    AssertionsForClassTypes.assertThat(result).usingRecursiveComparison().isEqualTo(outbox);

    Mockito.verify(outboxProcess, Mockito.never()).process(Mockito.any(Outbox.class));
    Mockito.verify(outboxRepositoryWritePort, Mockito.never())
        .upsertByEventIdentity(Mockito.any(Outbox.class));
  }

  @Test
  void processShouldKeepEventPendingWhenPublishingFails() {
    final Outbox outbox = OutboxTestDataBuilder.builder().build().outbox();
    final Outbox processing =
        outbox.toBuilder()
            .status(StatusVo.PROCESSING)
            .retryCount(1)
            .processedAt(DateVoFactory.from(TestTime.FIXED_INSTANT))
            .build();
    final Outbox pending = processing.toBuilder().status(StatusVo.PENDING).build();
    final SchedulerOutboxConfigurationVo schedulerConfig =
        SchedulerOutboxConfigurationVoTestDataBuilder.builder()
            .build()
            .schedulerOutboxConfigurationVo();

    Mockito.when(schedulerConfigurationPort.getOutboxScheduler()).thenReturn(schedulerConfig);
    Mockito.when(outboxRepositoryWritePort.claimPendingForProcessing(Mockito.any(Outbox.class)))
        .thenReturn(Optional.of(processing));
    Mockito.when(outboxRepositoryWritePort.upsertByEventIdentity(Mockito.any(Outbox.class)))
        .thenAnswer(AdditionalAnswers.returnsFirstArg());
    Mockito.doThrow(new RuntimeException("broker unavailable"))
        .when(outboxProcess)
        .process(Mockito.any(Outbox.class));

    instantMock.when(Instant::now).thenReturn(TestTime.FIXED_INSTANT);
    final Outbox result = outboxService.process(outbox);

    AssertionsForClassTypes.assertThat(result).usingRecursiveComparison().isEqualTo(pending);

    Mockito.verify(outboxRepositoryWritePort).claimPendingForProcessing(processing);
    Mockito.verify(outboxProcess).process(processing);
    Mockito.verify(outboxRepositoryWritePort).upsertByEventIdentity(pending);
  }

  @Test
  void processShouldPublishEventOnNextRetryAfterInitialFailure() {
    final Outbox outbox = OutboxTestDataBuilder.builder().build().outbox();
    final Outbox firstProcessing =
        outbox.toBuilder()
            .status(StatusVo.PROCESSING)
            .retryCount(1)
            .processedAt(DateVoFactory.from(TestTime.FIXED_INSTANT))
            .build();
    final Outbox pendingRetry = firstProcessing.toBuilder().status(StatusVo.PENDING).build();
    final Outbox secondProcessing =
        pendingRetry.toBuilder()
            .status(StatusVo.PROCESSING)
            .retryCount(2)
            .processedAt(DateVoFactory.from(TestTime.FIXED_INSTANT))
            .build();
    final Outbox published = secondProcessing.toBuilder().status(StatusVo.PROCESSED).build();
    final SchedulerOutboxConfigurationVo schedulerConfig =
        SchedulerOutboxConfigurationVoTestDataBuilder.builder()
            .build()
            .schedulerOutboxConfigurationVo();

    Mockito.when(schedulerConfigurationPort.getOutboxScheduler()).thenReturn(schedulerConfig);
    Mockito.when(outboxRepositoryWritePort.claimPendingForProcessing(Mockito.any(Outbox.class)))
        .thenReturn(Optional.of(firstProcessing), Optional.of(secondProcessing));
    Mockito.when(outboxRepositoryWritePort.upsertByEventIdentity(Mockito.any(Outbox.class)))
        .thenAnswer(AdditionalAnswers.returnsFirstArg());
    Mockito.doThrow(new RuntimeException("broker unavailable"))
        .doNothing()
        .when(outboxProcess)
        .process(Mockito.any(Outbox.class));

    instantMock.when(Instant::now).thenReturn(TestTime.FIXED_INSTANT);
    final Outbox firstAttempt = outboxService.process(outbox);
    final Outbox secondAttempt = outboxService.process(firstAttempt);

    AssertionsForClassTypes.assertThat(firstAttempt)
        .usingRecursiveComparison()
        .isEqualTo(pendingRetry);
    AssertionsForClassTypes.assertThat(secondAttempt)
        .usingRecursiveComparison()
        .isEqualTo(published);

    Mockito.verify(outboxRepositoryWritePort).claimPendingForProcessing(firstProcessing);
    Mockito.verify(outboxRepositoryWritePort).claimPendingForProcessing(secondProcessing);
    Mockito.verify(outboxProcess).process(firstProcessing);
    Mockito.verify(outboxProcess).process(secondProcessing);
    Mockito.verify(outboxRepositoryWritePort).upsertByEventIdentity(pendingRetry);
    Mockito.verify(outboxRepositoryWritePort).upsertByEventIdentity(published);
  }

  @Test
  void processShouldMarkEventFailedWhenMaxRetriesReached() {
    final Outbox outbox = OutboxTestDataBuilder.builder().retryCount(5).build().outbox();
    final Outbox processing =
        outbox.toBuilder()
            .status(StatusVo.PROCESSING)
            .retryCount(6)
            .processedAt(DateVoFactory.from(TestTime.FIXED_INSTANT))
            .build();
    final Outbox expectedFailed = processing.toBuilder().status(StatusVo.FAILED).build();
    final SchedulerOutboxConfigurationVo schedulerConfig =
        SchedulerOutboxConfigurationVoTestDataBuilder.builder()
            .maxRetries(5)
            .build()
            .schedulerOutboxConfigurationVo();
    Mockito.when(schedulerConfigurationPort.getOutboxScheduler()).thenReturn(schedulerConfig);
    Mockito.when(outboxRepositoryWritePort.claimPendingForProcessing(Mockito.any(Outbox.class)))
        .thenReturn(Optional.of(processing));
    Mockito.when(outboxRepositoryWritePort.upsertByEventIdentity(Mockito.any(Outbox.class)))
        .thenAnswer(AdditionalAnswers.returnsFirstArg());

    instantMock.when(Instant::now).thenReturn(TestTime.FIXED_INSTANT);
    final Outbox result = outboxService.process(outbox);

    AssertionsForClassTypes.assertThat(result).usingRecursiveComparison().isEqualTo(expectedFailed);

    Mockito.verify(outboxRepositoryWritePort).claimPendingForProcessing(processing);
    Mockito.verify(outboxRepositoryWritePort).upsertByEventIdentity(expectedFailed);
    Mockito.verify(outboxProcess, Mockito.never()).process(Mockito.any(Outbox.class));
  }
}
