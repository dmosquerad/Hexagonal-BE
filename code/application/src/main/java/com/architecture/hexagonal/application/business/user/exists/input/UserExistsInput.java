package com.architecture.hexagonal.application.business.user.exists.input;

import java.util.UUID;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class UserExistsInput {

  UUID userId;
}
