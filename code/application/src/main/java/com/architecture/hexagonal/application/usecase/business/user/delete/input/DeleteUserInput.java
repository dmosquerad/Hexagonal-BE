package com.architecture.hexagonal.application.usecase.business.user.delete.input;

import java.util.UUID;
import lombok.Builder;

@Builder
public record DeleteUserInput(UUID userId) {}
