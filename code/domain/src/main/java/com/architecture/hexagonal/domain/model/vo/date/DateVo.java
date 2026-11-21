package com.architecture.hexagonal.domain.model.vo.date;

import java.time.Instant;
import java.time.ZoneId;
import lombok.Builder;

@Builder
public record DateVo(Instant date, ZoneId zoneId) {}
