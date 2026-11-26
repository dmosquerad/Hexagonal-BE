package com.architecture.hexagonal.application.model.command.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.Builder;

@Builder
public record UpdateUserCommand(
    @NotNull UUID userId, @NotNull String name, @Email @NotNull String email) {}
