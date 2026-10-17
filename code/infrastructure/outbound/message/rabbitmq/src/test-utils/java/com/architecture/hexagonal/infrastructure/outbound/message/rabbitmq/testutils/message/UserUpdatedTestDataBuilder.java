package com.architecture.hexagonal.infrastructure.outbound.message.rabbitmq.testutils.message;

import com.architecture.hexagonal.contract.outbound.rabbitmq.model.MessageHeader;
import com.architecture.hexagonal.contract.outbound.rabbitmq.model.UserData;
import com.architecture.hexagonal.contract.outbound.rabbitmq.model.UserUpdated;
import lombok.Builder;

@Builder
public class UserUpdatedTestDataBuilder {

    @Builder.Default
    private MessageHeader messageHeader = MessageHeaderTestDataBuilder.builder().build().messageHeader();

    @Builder.Default
    private UserData userData = UserDataTestDataBuilder.builder().build().userData();

    public UserUpdated userUpdated() {
        final UserUpdated userUpdated = new UserUpdated();
        userUpdated.setMessageHeader(messageHeader);
        userUpdated.setData(userData);

        return userUpdated;
    }
}
