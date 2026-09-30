package com.architecture.hexagonal.application.usecase.business.user.patch.input;

import java.util.UUID;
import lombok.Builder;

@Builder
public record PatchUserInput(UUID userId, String name, String email) {}
