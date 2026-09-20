package com.architecture.hexagonal.infrastructure.outbound.database.mongodb.adapter;

import com.architecture.hexagonal.domain.model.entity.outbox.Outbox;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.config.MongodbIT;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.config.MongodbTestApplication;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.data.OutboxDao;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.mapper.outbox.OutboxDaoMapper;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.mapper.outbox.OutboxDoFromMongodbMapper;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.repository.OutboxWriteMongodbRepository;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.testutils.dao.OutboxDaoTestDataBuilder;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.testutils.data.aggregate.OutboxTestDataBuilder;

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
  void save_shouldPersistOutboxEventAndReturnOutboxDo() {
    final Outbox outbox =
        OutboxTestDataBuilder.builder().processedAt(null).build().outboxEventDo();

      final OutboxDao outboxDao =
              OutboxDaoTestDataBuilder.builder().processedAt(null).build().outboxEventDao();

    Mockito.when(outboxWriteMongodbRepository.findAndSaveWithMerge(Mockito.any(OutboxDao.class))).thenReturn(outboxDao);

    final Outbox result = outboxWriteMongodbAdapterImpl.save(outbox);

    AssertionsForClassTypes.assertThat(result).usingRecursiveComparison()
            .ignoringFieldsOfTypes(UUID.class)
            .isEqualTo(outbox);

    Mockito.verify(outboxDaoMapper).toOutboxEventDao(outbox);
    Mockito.verify(outboxWriteMongodbRepository).findAndSaveWithMerge(outboxDao);
    Mockito.verify(outboxDoFromMongodbMapper).toOutboxEvent(outboxDao);
  }
}
