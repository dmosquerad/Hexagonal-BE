package com.architecture.hexagonal.infrastructure.outbound.database.mongodb.testutils.data.payload;

import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.data.payload.MessageHeaderDao;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.data.payload.PayloadDao;
import com.architecture.hexagonal.domain.testutils.data.model.entity.user.UserTestDataBuilder;
import lombok.Builder;

@Builder
public class PayloadDaoTestDataBuilder {

    @Builder.Default private MessageHeaderDao messageHeaderDao = MessageHeaderDaoTestDataBuilder.builder().build().messageHeaderDao();

    @Builder.Default private Object data = UserTestDataBuilder.builder().build().user();

    public PayloadDao payloadDao() {
        final PayloadDao payloadDao = new PayloadDao();
        payloadDao.setMessageHeader(messageHeaderDao);
        payloadDao.setData(data);
        return payloadDao;
    }
}
