package com.architecture.hexagonal.infrastructure.inbound.execution.inbox.executor;

import com.architecture.hexagonal.application.port.configuration.SchedulerConfigurationPort;
import com.architecture.hexagonal.domain.model.vo.SchedulerOutboxConfigurationVo;
import com.architecture.hexagonal.infrastructure.inbound.execution.inbox.config.TestApplication;
import com.architecture.hexagonal.infrastructure.inbound.execution.inbox.data.vo.SchedulerInboxConfigurationVoTestDataBuilder;
import com.architecture.hexagonal.infrastructure.inbound.execution.inbox.executor.impl.InboxSchedulerImpl;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = {TestApplication.class, InboxSchedulerImpl.class})
class InboxSchedulerIntegrationTest {

    @Autowired
    private InboxSchedulerImpl inboxSchedulerImpl;

    @MockitoBean
    private SchedulerConfigurationPort schedulerConfigurationPort;

    @MockitoBean
    private ScheduledTaskRegistrar scheduledTaskRegistrar;

    @Test
    void shouldConfigureTasksWithCorrectInterval() {
        SchedulerOutboxConfigurationVo schedulerConfig = SchedulerInboxConfigurationVoTestDataBuilder.builder().build().schedulerInboxConfigurationVo();

        Mockito.when(schedulerConfigurationPort.getSchedulerOutbox()).thenReturn(schedulerConfig);

        inboxSchedulerImpl.configureTasks(scheduledTaskRegistrar);

        ArgumentCaptor<Runnable> scheduleProcessCaptor = ArgumentCaptor.forClass(Runnable.class);
        ArgumentCaptor<Duration> durationCaptor = ArgumentCaptor.forClass(Duration.class);

        Mockito.verify(scheduledTaskRegistrar).addFixedDelayTask(scheduleProcessCaptor.capture(), durationCaptor.capture());
        Duration expectedDuration = schedulerConfig.pollingInterval();
        Duration capturedDuration = durationCaptor.getValue();
        
        assertEquals(expectedDuration, capturedDuration);

        Mockito.verify(schedulerConfigurationPort).getSchedulerOutbox();

        scheduleProcessCaptor.getValue().run();
    }
}
