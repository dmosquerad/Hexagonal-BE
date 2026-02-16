package com.architecture.hexagonal.domain.input.command;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class CreateUserCommand {
  String email;
  String name;
  String role;
}
