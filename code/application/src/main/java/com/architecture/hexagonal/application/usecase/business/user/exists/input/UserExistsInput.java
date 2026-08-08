package com.architecture.hexagonal.application.usecase.business.user.exists.input;

import java.util.UUID;
import lombok.Builder;

@Builder
public record UserExistsInput(UUID userId) {}
