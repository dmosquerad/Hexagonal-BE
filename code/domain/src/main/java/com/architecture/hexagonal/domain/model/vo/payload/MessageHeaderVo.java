package com.architecture.hexagonal.domain.model.vo.payload;

import com.architecture.hexagonal.domain.model.vo.time.DateVo;
import java.util.UUID;
import lombok.Builder;

@Builder
public record MessageHeaderVo(UUID messageId, DateVo messageDate) {}
