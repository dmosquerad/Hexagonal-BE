package com.architecture.hexagonal.application.business.user.create.input;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class CreateUserCommand {

  String email;
  String name;
}
