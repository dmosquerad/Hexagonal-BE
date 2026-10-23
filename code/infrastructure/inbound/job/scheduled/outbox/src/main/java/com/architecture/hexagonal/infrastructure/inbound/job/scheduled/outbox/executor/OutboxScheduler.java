package com.architecture.hexagonal.infrastructure.inbound.job.scheduled.outbox.executor;

public interface OutboxScheduler {

  void scheduleRetry();
}
