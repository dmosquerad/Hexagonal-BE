package com.architecture.hexagonal.infrastructure.outbound.message.rabbitmq.testutils.data.message;

import com.architecture.hexagonal.domain.model.vo.MessageHeaderVo;
import com.architecture.hexagonal.infrastructure.outbound.message.rabbitmq.data.MessageHeader;
import com.architecture.hexagonal.infrastructure.outbound.message.rabbitmq.data.User;
import com.architecture.hexagonal.infrastructure.outbound.message.rabbitmq.data.UserUpdated;
import lombok.Builder;

@Builder
public class UserUpdatedTestDataBuilder {

    @Builder.Default
    private MessageHeaderVo messageHeader = MessageHeaderVoTestDataBuilder.builder().build().messageHeaderVo();

    @Builder.Default
    private User user = UserTestDataBuilder.builder().build().user();

    public UserUpdated userUpdated() {
        final UserUpdated userUpdated = new UserUpdated();
        userUpdated.setMessageHeader(messageHeader);
        userUpdated.setData(user);

        return userUpdated;
    }
}
