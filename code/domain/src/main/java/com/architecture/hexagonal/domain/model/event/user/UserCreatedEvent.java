package com.architecture.hexagonal.domain.model.event.user;

import java.util.UUID;
import lombok.Builder;

@Builder
public record UserCreatedEvent(UUID userId, String name, String email) {}
