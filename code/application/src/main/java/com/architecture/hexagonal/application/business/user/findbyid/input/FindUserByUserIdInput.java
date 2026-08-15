package com.architecture.hexagonal.application.business.user.findbyid.input;

import java.util.UUID;
import lombok.Builder;

@Builder
public record FindUserByUserIdInput(UUID userId) {}
