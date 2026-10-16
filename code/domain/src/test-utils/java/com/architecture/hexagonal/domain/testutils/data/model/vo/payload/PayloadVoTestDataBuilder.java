package com.architecture.hexagonal.domain.testutils.data.model.vo.payload;

import com.architecture.hexagonal.domain.model.vo.payload.MessageHeaderVo;
import com.architecture.hexagonal.domain.model.vo.payload.PayloadVo;
import lombok.Builder;

@Builder
public class PayloadVoTestDataBuilder<T> {

    @Builder.Default private MessageHeaderVo messageHeaderVo = MessageHeaderVoTestDataBuilder.builder().build().messageHeaderVo();

    private T data;

    public PayloadVo payloadVo() {
        return PayloadVo.builder()
                .messageHeader(messageHeaderVo)
                .data(data)
                .build();
    }
}
