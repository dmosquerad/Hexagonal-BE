package com.architecture.hexagonal.application.cqrs.command.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class CreateUserCommand {
  @Email
  @NotNull
  String email;
  @NotNull
  String name;
}
