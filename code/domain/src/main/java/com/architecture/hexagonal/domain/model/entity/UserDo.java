package com.architecture.hexagonal.domain.model.entity;

import lombok.Builder;
import lombok.Value;

import java.util.UUID;

@Value
@Builder
public class UserDo {
    UUID userId;
    String name;
}
