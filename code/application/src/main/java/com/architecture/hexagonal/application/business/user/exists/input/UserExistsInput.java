package com.architecture.hexagonal.application.business.user.exists.input;

import java.util.UUID;
import lombok.Builder;

@Builder
public record UserExistsInput(UUID userId) {}
