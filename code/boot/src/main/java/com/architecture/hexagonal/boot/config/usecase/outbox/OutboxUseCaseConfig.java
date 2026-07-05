package com.architecture.hexagonal.boot.config.usecase.outbox;

import com.architecture.hexagonal.application.technical.outbox.block.usecase.impl.BlockOutboxEventUseCaseImpl;
import com.architecture.hexagonal.application.technical.outbox.find.usecase.impl.FindPendingOutboxEventsUseCaseImpl;
import com.architecture.hexagonal.application.technical.outbox.process.usecase.impl.ProcessOutboxEventUseCaseImpl;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({
  FindPendingOutboxEventsUseCaseImpl.class,
  BlockOutboxEventUseCaseImpl.class,
  ProcessOutboxEventUseCaseImpl.class
})
public class OutboxUseCaseConfig {}
