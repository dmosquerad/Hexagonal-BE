package com.architecture.hexagonal.application.usecase.business.user.findbyid.input;

import java.util.UUID;
import lombok.Builder;

@Builder
public record FindUserByUserIdInput(UUID userId) {}
