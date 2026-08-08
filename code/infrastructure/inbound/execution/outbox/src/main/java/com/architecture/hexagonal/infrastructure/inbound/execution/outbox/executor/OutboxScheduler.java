package com.architecture.hexagonal.infrastructure.inbound.execution.outbox.executor;

public interface OutboxScheduler {

  void scheduleRetry();
}
