package com.architecture.hexagonal.domain.port.out.outbox;

import com.architecture.hexagonal.domain.model.entity.outbox.Outbox;
import lombok.NonNull;

public interface OutboxProcess {

  void process(@NonNull Outbox outbox);
}
