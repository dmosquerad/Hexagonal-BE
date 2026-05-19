package com.architecture.hexagonal.application.feature.user.patch.command;

import java.util.UUID;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class PatchUserCommand {
  @NotNull
  UUID userId;
  String name;
  @Email
  String email;
}
