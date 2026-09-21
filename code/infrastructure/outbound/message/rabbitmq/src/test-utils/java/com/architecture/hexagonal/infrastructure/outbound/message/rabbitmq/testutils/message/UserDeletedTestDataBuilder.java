package com.architecture.hexagonal.infrastructure.outbound.message.rabbitmq.testutils.message;

import com.architecture.hexagonal.domain.model.entity.user.User;
import com.architecture.hexagonal.domain.model.vo.outbox.MessageHeaderVo;
import com.architecture.hexagonal.infrastructure.outbound.message.rabbitmq.data.UserDeleted;
import com.architecture.hexagonal.infrastructure.outbound.message.rabbitmq.testutils.model.entity.user.UserTestDataBuilder;
import com.architecture.hexagonal.infrastructure.outbound.message.rabbitmq.testutils.model.vo.outbox.MessageHeaderVoTestDataBuilder;
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
