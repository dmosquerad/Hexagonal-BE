package com.architecture.hexagonal.domain.input.query;

import java.util.UUID;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class UserExistsQuery {
  private UUID userId;
}
