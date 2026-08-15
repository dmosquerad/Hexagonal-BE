package com.architecture.hexagonal.application.business.user.update.input;

import java.util.UUID;
import lombok.Builder;

@Builder
public record UpdateUserInput(UUID userId, String name, String email) {}
