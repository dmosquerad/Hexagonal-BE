package com.architecture.hexagonal.application.feature.user.delete.command;

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
