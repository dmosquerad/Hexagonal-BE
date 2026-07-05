package com.architecture.hexagonal.application.port.outbox;

import com.architecture.hexagonal.domain.model.entity.OutboxDo;
import lombok.NonNull;

public interface OutboxProcessPort {

  void process(@NonNull OutboxDo outboxDo);
}
