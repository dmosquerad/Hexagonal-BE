package com.architecture.hexagonal.infrastructure.outbound.event.rabbitmq.testutils.message;

import com.architecture.hexagonal.contract.outbound.event.rabbitmq.model.MessageHeader;
import com.architecture.hexagonal.contract.outbound.event.rabbitmq.model.UserData;
import com.architecture.hexagonal.contract.outbound.event.rabbitmq.model.UserDeleted;
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
