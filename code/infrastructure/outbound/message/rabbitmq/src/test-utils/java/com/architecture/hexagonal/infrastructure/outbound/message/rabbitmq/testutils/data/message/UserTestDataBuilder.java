package com.architecture.hexagonal.infrastructure.outbound.message.rabbitmq.testutils.data.message;

import com.architecture.hexagonal.infrastructure.outbound.message.rabbitmq.data.User;
import com.architecture.hexagonal.infrastructure.outbound.message.rabbitmq.data.UserUpdated;
import lombok.Builder;

import java.util.UUID;

@Builder
public class UserTestDataBuilder {

    @Builder.Default
    private UUID userId = UUID.fromString("4059510b-ceb3-4d4c-913e-1759acbd62a4");

    @Builder.Default
    private String name = "Test User";

    @Builder.Default
    private String email = "test@example.com";

    public User user() {
        final User user = new User();
        user.setUserId(userId);
        user.setName(name);
        user.setEmail(email);

        return user;
    }
}
