package com.architecture.hexagonal.application.input.command;

import java.util.UUID;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class UpdateUserCommand {
  @NotNull
  UUID userId;
  @NotNull
  String name;
  @Email
  @NotNull
  String email;
}