package com.architecture.hexagonal.domain.model.entity;

import java.util.UUID;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class UserDo {
  UUID userId;
  String name;
}
