package com.architecture.hexagonal.application.usecase.technical.outbox.process.input;

import com.architecture.hexagonal.domain.model.entity.outbox.Outbox;
import lombok.Builder;

@Builder
public record ProcessOutboxInput(Outbox outbox) {}
