package com.architecture.hexagonal.infrastructure.outbound.message.outbox.testutils.data.aggregate.outbox;

import com.architecture.hexagonal.domain.model.vo.outbox.PayloadVo;
import com.architecture.hexagonal.domain.model.entity.user.User;
import com.architecture.hexagonal.domain.model.vo.outbox.MessageHeaderVo;
import com.architecture.hexagonal.infrastructure.outbound.message.outbox.testutils.data.aggregate.user.UserTestDataBuilder;
import lombok.Builder;

@Builder
public class PayloadTestDataBuilder {

    @Builder.Default private MessageHeaderVo messageHeaderVo = MessageHeaderVo.builder().build();

    @Builder.Default private User user = UserTestDataBuilder.builder().build().user();

    public PayloadVo payload() {
        return PayloadVo.builder()
                .messageHeader(messageHeaderVo)
                .data(user)
                .build();
    }
}
