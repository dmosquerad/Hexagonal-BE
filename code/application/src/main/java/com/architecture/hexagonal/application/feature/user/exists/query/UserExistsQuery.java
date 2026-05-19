package com.architecture.hexagonal.application.feature.user.exists.query;

import java.util.UUID;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class UserExistsQuery {
  @NotNull
  UUID userId;
}
