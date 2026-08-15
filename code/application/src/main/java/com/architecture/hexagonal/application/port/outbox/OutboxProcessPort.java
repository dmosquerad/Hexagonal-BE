package com.architecture.hexagonal.application.port.outbox;

import com.architecture.hexagonal.domain.model.aggregate.outbox.Outbox;
import lombok.NonNull;

public interface OutboxProcessPort {

  void process(@NonNull Outbox outbox);
}
