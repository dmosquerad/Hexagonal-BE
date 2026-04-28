package com.architecture.hexagonal.infrastructure.outbound.message.data;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class UserCreated {

    String userId;
    String name;
    String email;
}
