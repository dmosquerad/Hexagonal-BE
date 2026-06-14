package com.architecture.hexagonal.application.user.exists.input;

import java.util.UUID;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class UserExistsQuery {

  UUID userId;
}
