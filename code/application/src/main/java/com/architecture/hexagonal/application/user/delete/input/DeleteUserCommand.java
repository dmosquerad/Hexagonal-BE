package com.architecture.hexagonal.application.user.delete.input;

import java.util.UUID;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class DeleteUserCommand {
  UUID userId;
}
