package com.architecture.hexagonal.application.port.database;

import com.architecture.hexagonal.domain.model.entity.OutboxDo;
import lombok.NonNull;

public interface OutboxRepositoryWritePort {
  OutboxDo save(@NonNull OutboxDo outboxDo);
}
