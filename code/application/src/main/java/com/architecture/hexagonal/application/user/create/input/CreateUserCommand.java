package com.architecture.hexagonal.application.user.create.input;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class CreateUserCommand {

  String email;
  String name;
}
