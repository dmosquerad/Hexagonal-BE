package com.architecture.hexagonal.infrastructure.outbound.message.testutils.data.entity;

import com.architecture.hexagonal.domain.model.entity.UserDo;
import lombok.Builder;

import java.util.UUID;

@Builder
public class UserDoTestDataBuilder {

    @Builder.Default
    private UUID userId = UUID.fromString("4059510b-ceb3-4d4c-913e-1759acbd62a4");

    @Builder.Default
    private String name = "Test User";

    public UserDo userDo() {
        return UserDo.builder()
                .userId(userId)
                .name(name)
                .build();
    }
}
