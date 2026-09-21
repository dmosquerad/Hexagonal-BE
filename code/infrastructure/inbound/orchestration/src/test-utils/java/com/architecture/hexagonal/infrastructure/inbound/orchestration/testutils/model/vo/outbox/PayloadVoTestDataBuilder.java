package com.architecture.hexagonal.infrastructure.inbound.orchestration.testutils.model.vo.outbox;

import com.architecture.hexagonal.domain.model.vo.outbox.PayloadVo;
import com.architecture.hexagonal.domain.model.entity.user.User;
import com.architecture.hexagonal.domain.model.vo.outbox.MessageHeaderVo;

import com.architecture.hexagonal.infrastructure.inbound.orchestration.testutils.model.entity.user.UserTestDataBuilder;
import lombok.Builder;

@Builder
public class PayloadVoTestDataBuilder {

    @Builder.Default private MessageHeaderVo messageHeaderVo = MessageHeaderVoTestDataBuilder.builder().build().messageHeaderVo();

    @Builder.Default private User user = UserTestDataBuilder.builder().build().user();

    public PayloadVo payload() {
        return PayloadVo.builder()
                .messageHeader(messageHeaderVo)
                .data(user)
                .build();
    }
}
