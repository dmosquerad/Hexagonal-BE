package com.architecture.hexagonal.infrastructure.outbound.database.mongodb.testutils.data.payload;

import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.data.payload.MessageHeaderDao;
import com.architecture.hexagonal.domain.testutils.data.time.TestClock;
import lombok.Builder;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.UUID;

@Builder
public class MessageHeaderDaoTestDataBuilder {

    @Builder.Default
    private UUID messageId = UUID.fromString("4059510b-ceb3-4d4c-913e-1759acbd63b1");

    @Builder.Default
    private ZonedDateTime messageDate = TestClock.FIXED_INSTANT.atZone(ZoneOffset.UTC);

    public MessageHeaderDao messageHeaderDao() {
        final MessageHeaderDao messageHeaderDao = new MessageHeaderDao();

        messageHeaderDao.setMessageId(messageId);
        messageHeaderDao.setMessageDate(messageDate);

        return messageHeaderDao;
    }
}
