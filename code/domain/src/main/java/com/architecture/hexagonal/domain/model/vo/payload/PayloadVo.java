package com.architecture.hexagonal.domain.model.vo.payload;

import lombok.Builder;

@Builder
public record PayloadVo(MessageHeaderVo messageHeader, Object data) {}
