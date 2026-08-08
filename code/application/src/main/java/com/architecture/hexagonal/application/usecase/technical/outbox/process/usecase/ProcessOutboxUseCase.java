package com.architecture.hexagonal.application.usecase.technical.outbox.process.usecase;

import com.architecture.hexagonal.application.usecase.technical.outbox.process.input.ProcessOutboxInput;
import com.architecture.hexagonal.domain.model.entity.outbox.Outbox;
import lombok.NonNull;

public interface ProcessOutboxUseCase {

  Outbox execute(@NonNull ProcessOutboxInput processOutboxInput);
}
