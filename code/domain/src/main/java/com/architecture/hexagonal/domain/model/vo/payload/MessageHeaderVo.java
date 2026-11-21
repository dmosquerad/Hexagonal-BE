package com.architecture.hexagonal.domain.model.vo.payload;

import com.architecture.hexagonal.domain.model.vo.date.DateVo;
import java.util.UUID;
import lombok.Builder;

@Builder
public record MessageHeaderVo(UUID messageId, DateVo messageDate) {}
