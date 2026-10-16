package com.architecture.hexagonal.infrastructure.outbound.database.mongodb.adapter.outbox;

import com.architecture.hexagonal.domain.model.entity.outbox.Outbox;
import com.architecture.hexagonal.domain.model.entity.user.User;
import com.architecture.hexagonal.domain.testutils.data.model.entity.outbox.OutboxTestDataBuilder;
import com.architecture.hexagonal.domain.testutils.data.model.entity.user.UserTestDataBuilder;
import com.architecture.hexagonal.domain.testutils.data.model.vo.payload.PayloadVoTestDataBuilder;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.config.MongodbIT;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.config.MongodbTestApplication;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.data.outbox.OutboxDao;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.mapper.outbox.OutboxDaoMapper;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.mapper.outbox.OutboxDoFromMongodbMapper;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.repository.outbox.OutboxWriteMongodbRepository;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.testutils.data.outbox.OutboxDaoTestDataBuilder;

import java.util.UUID;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

@SpringBootTest(classes = {OutboxRepositoryWriteMongodbAdapterImpl.class})
@ContextConfiguration(classes = MongodbTestApplication.class)
class OutboxWriteMongodbAdapterImplTestIT extends MongodbIT {

  @Autowired private OutboxRepositoryWriteMongodbAdapterImpl outboxWriteMongodbAdapterImpl;

  @MockitoSpyBean private OutboxDoFromMongodbMapper outboxDoFromMongodbMapper;

  @MockitoSpyBean private OutboxDaoMapper outboxDaoMapper;

  @MockitoBean private OutboxWriteMongodbRepository outboxWriteMongodbRepository;

  @Test
  void saveShouldPersistOutboxEventAndReturnOutboxDo() {
    final Outbox outbox =
        OutboxTestDataBuilder.<User>builder()
            .payload(
                PayloadVoTestDataBuilder.<User>builder()
                    .data(UserTestDataBuilder.builder().build().user())
                    .build()
                    .payloadVo())
            .processedAt(null)
            .build()
            .outbox();

      final OutboxDao outboxDao =
              OutboxDaoTestDataBuilder.builder().processedAt(null).build().outboxEventDao();

    Mockito.when(outboxWriteMongodbRepository.upsertByEventIdentity(Mockito.any(OutboxDao.class))).thenReturn(outboxDao);

    final Outbox result = outboxWriteMongodbAdapterImpl.upsertByEventIdentity(outbox);

    AssertionsForClassTypes.assertThat(result).usingRecursiveComparison()
            .ignoringFieldsOfTypes(UUID.class)
            .isEqualTo(outbox);

    Mockito.verify(outboxDaoMapper).toOutboxDao(outbox);
    Mockito.verify(outboxWriteMongodbRepository).upsertByEventIdentity(outboxDao);
    Mockito.verify(outboxDoFromMongodbMapper).toOutbox(outboxDao);
  }
}
