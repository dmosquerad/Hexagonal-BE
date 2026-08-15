package com.architecture.hexagonal.infrastructure.inbound.execution.outbox.executor;

import com.architecture.hexagonal.application.port.configuration.SchedulerConfigurationPort;
import com.architecture.hexagonal.domain.model.vo.SchedulerOutboxConfigurationVo;
import com.architecture.hexagonal.infrastructure.inbound.contract.orchestration.generated.retry.RetryOutboxeventCommandDto;
import com.architecture.hexagonal.infrastructure.inbound.execution.outbox.data.vo.SchedulerOutboxConfigurationVoTestDataBuilder;
import com.architecture.hexagonal.infrastructure.inbound.execution.outbox.config.TestApplication;
import com.architecture.hexagonal.infrastructure.inbound.execution.outbox.executor.impl.OutboxSchedulerImpl;
import com.architecture.hexagonal.infrastructure.inbound.orchestration.dispatcher.command.CommandBus;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
        SchedulerOutboxConfigurationVo schedulerConfig = SchedulerOutboxConfigurationVoTestDataBuilder.builder().build().schedulerOutboxConfigurationVo();

        Mockito.when(schedulerConfigurationPort.getSchedulerOutbox()).thenReturn(schedulerConfig);

        outboxSchedulerImpl.configureTasks(scheduledTaskRegistrar);

        ArgumentCaptor<Runnable> scheduleRetryCaptor = ArgumentCaptor.forClass(Runnable.class);
        ArgumentCaptor<Duration> durationCaptor = ArgumentCaptor.forClass(Duration.class);

        Mockito.verify(scheduledTaskRegistrar).addFixedDelayTask(scheduleRetryCaptor.capture(), durationCaptor.capture());
        Duration expectedDuration = schedulerConfig.pollingInterval();
        Duration capturedDuration = durationCaptor.getValue();
        
        assertEquals(expectedDuration, capturedDuration);

        Mockito.verify(schedulerConfigurationPort).getSchedulerOutbox();

        scheduleRetryCaptor.getValue().run();
        Mockito.verify(commandBus).execute(Mockito.any(RetryOutboxeventCommandDto.class));
    }
}
