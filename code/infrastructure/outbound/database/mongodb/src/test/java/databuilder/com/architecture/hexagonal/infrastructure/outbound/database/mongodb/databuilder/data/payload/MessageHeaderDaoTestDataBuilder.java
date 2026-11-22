package com.architecture.hexagonal.infrastructure.outbound.database.mongodb.databuilder.data.payload;

import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.data.payload.MessageHeaderDao;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.data.time.DateDao;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.databuilder.data.time.DateDaoTestDataBuilder;
import lombok.Builder;

import java.util.UUID;

@Builder
public class MessageHeaderDaoTestDataBuilder {

    @Builder.Default
    private UUID messageId = UUID.fromString("4059510b-ceb3-4d4c-913e-1759acbd63b1");

    @Builder.Default
    private DateDao messageDate = DateDaoTestDataBuilder.builder().build().dateDao();

    public MessageHeaderDao messageHeaderDao() {
        final MessageHeaderDao messageHeaderDao = new MessageHeaderDao();

        messageHeaderDao.setMessageId(messageId);
        messageHeaderDao.setMessageDate(messageDate);

        return messageHeaderDao;
    }
}
