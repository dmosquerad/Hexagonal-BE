package com.architecture.hexagonal.domain.model.vo.outbox;

import lombok.Builder;

@Builder
public record PayloadVo(MessageHeaderVo messageHeader, Object data) {}
