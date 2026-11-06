package com.architecture.hexagonal.domain.model.vo.payload;

import java.time.OffsetDateTime;
import java.util.UUID;
import lombok.Builder;

@Builder
public record MessageHeaderVo(UUID messageId, OffsetDateTime messageDate) {}
