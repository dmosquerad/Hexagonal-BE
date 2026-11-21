package com.architecture.hexagonal.infrastructure.outbound.database.mongodb.databuilder.data.payload;

import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.data.date.DateVoDao;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.data.payload.MessageHeaderDao;
import java.util.UUID;

import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.databuilder.data.date.DateVoDaoTestDataBuilder;
import lombok.Builder;

@Builder
public class MessageHeaderDaoTestDataBuilder {

    @Builder.Default
    private UUID messageId = UUID.fromString("4059510b-ceb3-4d4c-913e-1759acbd63b1");

    @Builder.Default
    private DateVoDao dateVoDao = DateVoDaoTestDataBuilder.builder().build().dateVoDao();

    public MessageHeaderDao messageHeaderDao() {
        final MessageHeaderDao messageHeaderDao = new MessageHeaderDao();

        messageHeaderDao.setMessageId(messageId);
        messageHeaderDao.setMessageDate(dateVoDao);

        return messageHeaderDao;
    }
}
