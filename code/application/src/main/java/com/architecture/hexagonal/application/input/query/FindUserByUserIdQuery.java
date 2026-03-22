package com.architecture.hexagonal.application.input.query;

import java.util.UUID;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class FindUserByUserIdQuery {
  @NotNull
  UUID userId;
}
