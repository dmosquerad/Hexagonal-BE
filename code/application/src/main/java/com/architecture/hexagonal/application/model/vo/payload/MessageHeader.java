package com.architecture.hexagonal.application.model.vo.payload;

import com.architecture.hexagonal.application.model.vo.time.Date;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.Builder;

@Builder
public record MessageHeader(@NotNull UUID messageId, @Valid @NotNull Date messageDate) {}
