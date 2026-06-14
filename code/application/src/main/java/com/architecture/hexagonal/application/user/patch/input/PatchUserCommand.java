package com.architecture.hexagonal.application.user.patch.input;

import java.util.UUID;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class PatchUserCommand {

  UUID userId;
  String name;
  String email;
}
