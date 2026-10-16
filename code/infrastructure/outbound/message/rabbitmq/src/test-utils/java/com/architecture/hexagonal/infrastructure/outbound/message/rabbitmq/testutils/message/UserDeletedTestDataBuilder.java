package com.architecture.hexagonal.infrastructure.outbound.message.rabbitmq.testutils.message;

import com.architecture.hexagonal.contract.outbound.rabbitmq.model.MessageHeader;
import com.architecture.hexagonal.contract.outbound.rabbitmq.model.UserData;
import com.architecture.hexagonal.contract.outbound.rabbitmq.model.UserDeleted;
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
