package com.architecture.hexagonal.application.model.command.inbox;

import com.architecture.hexagonal.application.model.vo.payload.MessageHeader;
import com.architecture.hexagonal.application.model.vo.time.Date;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder(toBuilder = true)
public record RegisterInboxCommand(
    @Valid @NotNull MessageHeader messageHeader,
    @NotNull Object data,
    Object aggregateId,
    @NotNull String aggregateType,
    @NotNull String action,
    @Valid @NotNull Date createdAt) {}
