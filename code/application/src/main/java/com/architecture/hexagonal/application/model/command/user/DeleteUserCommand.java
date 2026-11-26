package com.architecture.hexagonal.application.model.command.user;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.Builder;

@Builder
public record DeleteUserCommand(@NotNull UUID userId) {}
