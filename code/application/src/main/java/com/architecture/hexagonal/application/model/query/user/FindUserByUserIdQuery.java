package com.architecture.hexagonal.application.model.query.user;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.Builder;

@Builder
public record FindUserByUserIdQuery(@NotNull UUID userId) {}
