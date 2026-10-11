package com.architecture.hexagonal.infrastructure.outbound.outbox.testutils.model.entity.user;

import com.architecture.hexagonal.contract.outbound.rabbitmq.UserData;
import com.architecture.hexagonal.domain.model.entity.user.User;
import com.architecture.hexagonal.domain.model.vo.email.EmailVo;
import com.architecture.hexagonal.infrastructure.outbound.outbox.testutils.model.vo.email.EmailVoTestDataBuilder;
import lombok.Builder;

import java.util.UUID;

@Builder
public class UserDataTestDataBuilder {

    @Builder.Default
    private UUID userId = UUID.fromString("4059510b-ceb3-4d4c-913e-1759acbd62a4");

    @Builder.Default
    private String name = "Test User";

    @Builder.Default
    private EmailVo email = EmailVoTestDataBuilder.builder()
            .build()
            .emailVo();

    public UserData userData() {
        final UserData userData = new UserData();
        userData.setUserId(userId);
        userData.setName(name);
        userData.setEmail(email.getEmail());

        return userData;
    }
}
