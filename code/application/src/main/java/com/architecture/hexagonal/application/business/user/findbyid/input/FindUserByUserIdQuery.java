package com.architecture.hexagonal.application.business.user.findbyid.input;

import java.util.UUID;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class FindUserByUserIdQuery {

  UUID userId;
}
