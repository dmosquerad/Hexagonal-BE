package com.architecture.hexagonal.application.business.user.create.input;

import lombok.Builder;

@Builder
public record CreateUserInput(String email, String name) {}
