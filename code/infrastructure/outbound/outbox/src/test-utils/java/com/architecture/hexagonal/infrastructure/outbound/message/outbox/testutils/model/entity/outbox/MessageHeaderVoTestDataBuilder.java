package com.architecture.hexagonal.infrastructure.outbound.message.outbox.testutils.model.entity.outbox;

import com.architecture.hexagonal.domain.model.vo.outbox.MessageHeaderVo;
import com.architecture.hexagonal.infrastructure.outbound.message.outbox.testutils.time.TestClock;
import lombok.Builder;

import java.time.OffsetDateTime;
import java.util.UUID;

@Builder
public class MessageHeaderVoTestDataBuilder {

    @Builder.Default
    private UUID messageId = UUID.fromString("4059510b-ceb3-4d4c-913e-1759acbd63b1");

    @Builder.Default
    private OffsetDateTime messageDate = OffsetDateTime.now(TestClock.FIXED_CLOCK);

    public MessageHeaderVo messageHeaderVo() {
        return MessageHeaderVo.builder()
                .messageId(messageId)
                .messageDate(messageDate)
                .build();
    }
}
