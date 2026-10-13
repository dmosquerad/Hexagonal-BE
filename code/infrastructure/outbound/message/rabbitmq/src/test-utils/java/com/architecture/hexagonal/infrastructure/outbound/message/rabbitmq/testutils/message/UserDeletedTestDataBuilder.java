package com.architecture.hexagonal.infrastructure.outbound.message.rabbitmq.testutils.message;

import com.architecture.hexagonal.contract.outbound.rabbitmq.MessageHeader;
import com.architecture.hexagonal.contract.outbound.rabbitmq.UserData;
import com.architecture.hexagonal.contract.outbound.rabbitmq.UserDeleted;
import com.architecture.hexagonal.infrastructure.outbound.message.rabbitmq.testutils.model.entity.user.UserDataTestDataBuilder;
import com.architecture.hexagonal.infrastructure.outbound.message.rabbitmq.testutils.model.vo.outbox.MessageHeaderTestDataBuilder;
import lombok.Builder;

@Builder
public class UserDeletedTestDataBuilder {

    @Builder.Default
    private MessageHeader messageHeader = MessageHeaderTestDataBuilder.builder().build().messageHeader();

    @Builder.Default
    private UserData userData = UserDataTestDataBuilder.builder().build().userData();

    public UserDeleted userDeleted() {
        final UserDeleted userDeleted = new UserDeleted();
        userDeleted.setMessageHeader(messageHeader);
        userDeleted.setData(userData);

        return userDeleted;
    }
}
