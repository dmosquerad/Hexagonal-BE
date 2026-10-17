package com.architecture.hexagonal.infrastructure.inbound.execution.outbox.executor;

import com.architecture.hexagonal.application.port.out.scheduler.SchedulerConfigurationPort;
import com.architecture.hexagonal.domain.model.vo.outbox.SchedulerOutboxConfigurationVo;
import com.architecture.hexagonal.contract.bus.model.retry.command.RetryOutboxeventCommand;
import com.architecture.hexagonal.domain.testutils.data.model.vo.outbox.SchedulerOutboxConfigurationVoTestDataBuilder;
import com.architecture.hexagonal.infrastructure.inbound.execution.outbox.config.TestApplication;
import com.architecture.hexagonal.infrastructure.inbound.execution.outbox.executor.impl.OutboxSchedulerImpl;
import com.architecture.hexagonal.infrastructure.bus.command.CommandBus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.Duration;

@SpringBootTest(classes = {TestApplication.class, OutboxSchedulerImpl.class})
class OutboxSchedulerOutboxConfigIntegrationTest {

    @Autowired
    private OutboxSchedulerImpl outboxSchedulerImpl;

    @MockitoBean
    private SchedulerConfigurationPort schedulerConfigurationPort;

    @MockitoBean
    private CommandBus commandBus;

    @MockitoBean
    private ScheduledTaskRegistrar scheduledTaskRegistrar;

    @Test
    void shouldConfigureTasksWithCorrectInterval() {
        final SchedulerOutboxConfigurationVo schedulerConfig = SchedulerOutboxConfigurationVoTestDataBuilder.builder().build().schedulerOutboxConfigurationVo();

        Mockito.when(schedulerConfigurationPort.getOutboxScheduler()).thenReturn(schedulerConfig);

        outboxSchedulerImpl.configureTasks(scheduledTaskRegistrar);

        final ArgumentCaptor<Runnable> scheduleRetryCaptor = ArgumentCaptor.forClass(Runnable.class);
        final ArgumentCaptor<Duration> durationCaptor = ArgumentCaptor.forClass(Duration.class);

        Mockito.verify(scheduledTaskRegistrar).addFixedDelayTask(scheduleRetryCaptor.capture(), durationCaptor.capture());
        final Duration expectedDuration = schedulerConfig.pollingInterval();
        final Duration capturedDuration = durationCaptor.getValue();
        
        Assertions.assertEquals(expectedDuration, capturedDuration);

        Mockito.verify(schedulerConfigurationPort).getOutboxScheduler();

        scheduleRetryCaptor.getValue().run();
        Mockito.verify(commandBus).execute(Mockito.any(RetryOutboxeventCommand.class));
    }
}
