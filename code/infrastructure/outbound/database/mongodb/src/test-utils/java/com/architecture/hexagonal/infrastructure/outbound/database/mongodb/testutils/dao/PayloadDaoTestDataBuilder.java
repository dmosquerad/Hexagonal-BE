package com.architecture.hexagonal.infrastructure.outbound.database.mongodb.testutils.dao;

import com.architecture.hexagonal.domain.model.entity.user.User;
import com.architecture.hexagonal.domain.model.vo.outbox.MessageHeaderVo;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.data.PayloadDao;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.testutils.data.aggregate.UserTestDataBuilder;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.testutils.vo.MessageHeaderVoTestDataBuilder;
import lombok.Builder;

@Builder
public class PayloadDaoTestDataBuilder {

    @Builder.Default private MessageHeaderVo messageHeaderVo = MessageHeaderVoTestDataBuilder.builder().build().messageHeaderVo();

    @Builder.Default private User user = UserTestDataBuilder.builder().build().user();

    public PayloadDao payloadDao() {
        final PayloadDao payloadDao = new PayloadDao();
        payloadDao.setMessageHeader(messageHeaderVo);
        payloadDao.setData(user);

        return payloadDao;
    }
}
