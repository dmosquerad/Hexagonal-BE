package com.architecture.hexagonal.infrastructure.outbound.message.data;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class UserUpdated {

    String userId;
    String name;
    String email;
}
