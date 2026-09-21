package com.architecture.hexagonal.infrastructure.outbound.database.mongodb.testutils.model.vo.outbox;

import com.architecture.hexagonal.domain.model.vo.outbox.PayloadVo;
import com.architecture.hexagonal.domain.model.entity.user.User;
import com.architecture.hexagonal.domain.model.vo.outbox.MessageHeaderVo;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.testutils.model.entity.user.UserTestDataBuilder;
import lombok.Builder;

@Builder
public class PayloadTestDataBuilder {

    @Builder.Default private MessageHeaderVo messageHeader = MessageHeaderVoTestDataBuilder.builder().build().messageHeaderVo();

    @Builder.Default private User user = UserTestDataBuilder.builder().build().user();

    public PayloadVo payload() {
        return PayloadVo.builder()
                .messageHeader(messageHeader)
                .data(user)
                .build();
    }
}
