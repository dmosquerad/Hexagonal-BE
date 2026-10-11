package com.architecture.hexagonal.infrastructure.outbound.outbox.testutils.model.entity.outbox;

import com.architecture.hexagonal.domain.model.vo.outbox.PayloadVo;
import com.architecture.hexagonal.domain.model.vo.outbox.MessageHeaderVo;
import com.architecture.hexagonal.contract.outbound.rabbitmq.UserData;
import com.architecture.hexagonal.infrastructure.outbound.outbox.testutils.model.entity.user.UserDataTestDataBuilder;
import lombok.Builder;

@Builder
public class PayloadTestDataBuilder {

    @Builder.Default private MessageHeaderVo messageHeaderVo = MessageHeaderVoTestDataBuilder.builder().build().messageHeaderVo();

    @Builder.Default private UserData userData = UserDataTestDataBuilder.builder().build().userData();

    public PayloadVo payload() {
        return PayloadVo.builder()
                .messageHeader(messageHeaderVo)
                .data(userData)
                .build();
    }
}
