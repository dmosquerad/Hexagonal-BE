package com.architecture.hexagonal.infrastructure.outbound.database.mongodb.adapter;

import com.architecture.hexagonal.domain.model.aggregate.outbox.Outbox;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.config.MongodbIT;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.config.MongodbTestApplication;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.repository.OutboxWriteMongodbRepository;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.testutils.data.aggregate.OutboxTestDataBuilder;

import java.util.UUID;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest(classes = {OutboxRepositoryWriteMongodbAdapterImpl.class})
@ContextConfiguration(classes = MongodbTestApplication.class)
class OutboxWriteMongodbAdapterImplTestIT extends MongodbIT {

  @Autowired private OutboxRepositoryWriteMongodbAdapterImpl outboxWriteMongodbAdapterImpl;
  @Autowired private OutboxWriteMongodbRepository outboxWriteMongodbRepository;

  @Test
  void save_shouldPersistOutboxEventAndReturnOutboxDo() {
    final UUID eventId = UUID.randomUUID();
    final Outbox outbox =
        OutboxTestDataBuilder.builder().eventId(eventId).processedAt(null).build().outboxEventDo();

    AssertionsForClassTypes.assertThat(outboxWriteMongodbRepository.existsById(eventId)).isFalse();

      final Outbox result = outboxWriteMongodbAdapterImpl.save(outbox);

    AssertionsForClassTypes.assertThat(result).usingRecursiveComparison().isEqualTo(outbox);
    AssertionsForClassTypes.assertThat(outboxWriteMongodbRepository.existsById(eventId)).isTrue();
  }
}
