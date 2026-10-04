package com.architecture.hexagonal.infrastructure.outbound.database.mongodb.testutils.dao;

import com.architecture.hexagonal.domain.model.entity.user.User;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.data.MessageHeaderDao;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.data.PayloadDao;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.testutils.model.entity.user.UserTestDataBuilder;
import lombok.Builder;

@Builder
public class PayloadDaoTestDataBuilder {

    @Builder.Default private MessageHeaderDao messageHeaderDao = MessageHeaderDaoTestDataBuilder.builder().build().messageHeaderDao();

    @Builder.Default private User user = UserTestDataBuilder.builder().build().user();

    public PayloadDao payloadDao() {
        final PayloadDao payloadDao = new PayloadDao();
        payloadDao.setMessageHeader(messageHeaderDao);
        payloadDao.setData(user);

        return payloadDao;
    }
}
