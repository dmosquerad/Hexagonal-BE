package com.architecture.hexagonal.application.cqrs.query.request;

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
