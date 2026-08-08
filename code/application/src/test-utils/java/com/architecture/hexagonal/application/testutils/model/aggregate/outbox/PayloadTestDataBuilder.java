package com.architecture.hexagonal.application.testutils.model.aggregate.outbox;

import com.architecture.hexagonal.application.testutils.model.aggregate.user.UserTestDataBuilder;
import com.architecture.hexagonal.domain.model.vo.outbox.PayloadVo;
import com.architecture.hexagonal.domain.model.entity.user.User;
import com.architecture.hexagonal.domain.model.vo.outbox.MessageHeaderVo;
import lombok.Builder;

@Builder
public class PayloadTestDataBuilder {

    @Builder.Default private MessageHeaderVo messageHeaderVo = MessageHeaderVoTestDataBuilder.builder().build().messageHeaderVo();

    @Builder.Default private User user = UserTestDataBuilder.builder().build().user();

    public PayloadVo payload() {
        return PayloadVo.builder()
                .messageHeader(messageHeaderVo)
                .data(user)
                .build();
    }
}
