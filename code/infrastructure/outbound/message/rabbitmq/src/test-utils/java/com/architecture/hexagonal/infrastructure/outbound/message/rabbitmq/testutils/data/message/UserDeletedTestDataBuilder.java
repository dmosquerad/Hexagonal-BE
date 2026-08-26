package com.architecture.hexagonal.infrastructure.outbound.message.rabbitmq.testutils.data.message;

import com.architecture.hexagonal.domain.model.vo.MessageHeaderVo;
import com.architecture.hexagonal.infrastructure.outbound.message.rabbitmq.data.MessageHeader;
import com.architecture.hexagonal.infrastructure.outbound.message.rabbitmq.data.User;
import com.architecture.hexagonal.infrastructure.outbound.message.rabbitmq.data.UserDeleted;
import lombok.Builder;

@Builder
public class UserDeletedTestDataBuilder {

    @Builder.Default
    private MessageHeaderVo messageHeader = MessageHeaderVoTestDataBuilder.builder().build().messageHeaderVo();

    @Builder.Default
    private User user = UserTestDataBuilder.builder().build().user();

    public UserDeleted userDeleted() {
        final UserDeleted userDeleted = new UserDeleted();
        userDeleted.setMessageHeader(messageHeader);
        userDeleted.setData(user);

        return userDeleted;
    }
}
