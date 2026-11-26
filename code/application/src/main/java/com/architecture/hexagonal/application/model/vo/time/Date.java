package com.architecture.hexagonal.application.model.vo.time;

import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import lombok.Builder;

@Builder
public record Date(@NotNull Instant date, @NotNull String zone) {}
