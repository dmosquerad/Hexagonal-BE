package com.architecture.hexagonal.infrastructure.outbound.message.outbox.testutils.data.message;

import com.architecture.hexagonal.domain.model.vo.MessageHeaderVo;
import com.architecture.hexagonal.infrastructure.outbound.message.rabbitmq.data.User;
import com.architecture.hexagonal.infrastructure.outbound.message.rabbitmq.data.UserCreated;
import lombok.Builder;

@Builder
public class UserCreatedTestDataBuilder {

    @Builder.Default
    private MessageHeaderVo messageHeader = MessageHeaderVoTestDataBuilder.builder().build().messageHeaderVo();

    @Builder.Default
    private User user = UserTestDataBuilder.builder().build().user();

    public UserCreated userCreated() {
        final UserCreated userCreated = new UserCreated();
        userCreated.setMessageHeader(messageHeader);
        userCreated.setData(user);

        return userCreated;
    }
}