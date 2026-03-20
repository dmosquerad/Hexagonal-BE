package com.architecture.hexagonal.domain.input.command;

import java.util.UUID;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class DeleteUserCommand {
  UUID userId;
}
