package com.architecture.hexagonal.application.cqrs.command.request;

import java.util.UUID;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class DeleteUserCommand {
  @NotNull
  UUID userId;
}
