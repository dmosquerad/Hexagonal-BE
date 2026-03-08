package com.architecture.hexagonal.domain.data;

import java.util.UUID;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class User {
  UUID userId;
  String name;
  String email;
}
