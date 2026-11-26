package com.architecture.hexagonal.application.model.command.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record CreateUserCommand(@Email @NotNull String email, @NotNull String name) {}
