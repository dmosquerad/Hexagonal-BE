package com.architecture.hexagonal.infrastructure.outbound.message.rabbitmq.testutils.model.vo.outbox;

import com.architecture.hexagonal.contract.outbound.rabbitmq.MessageHeader;
import com.architecture.hexagonal.domain.model.vo.outbox.MessageHeaderVo;
import lombok.Builder;

import java.time.OffsetDateTime;
import java.util.UUID;

@Builder
public class MessageHeaderTestDataBuilder {

    @Builder.Default
    private UUID messageId = UUID.fromString("4059510b-ceb3-4d4c-913e-1759acbd63b1");

    @Builder.Default
    private OffsetDateTime messageDate = null;

    public MessageHeader messageHeader() {
        final MessageHeader messageHeader = new MessageHeader();
        messageHeader.setMessageId(messageId);
        messageHeader.setMessageDate(messageDate);

        return messageHeader;
    }
}
