package com.architecture.hexagonal.infrastructure.outbound.database.mongodb.adapter;

import com.architecture.hexagonal.domain.model.entity.OutboxDo;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.config.MongodbIT;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.config.MongodbTestApplication;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.repository.OutboxWriteMongodbRepository;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.testutils.data.OutboxEventDoTestDataBuilder;

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
    final OutboxDo outboxDo =
        OutboxEventDoTestDataBuilder.builder().eventId(eventId).processedAt(null).build().outboxEventDo();

    AssertionsForClassTypes.assertThat(outboxWriteMongodbRepository.existsById(eventId)).isFalse();

      final OutboxDo result = outboxWriteMongodbAdapterImpl.save(outboxDo);

    AssertionsForClassTypes.assertThat(result).usingRecursiveComparison().isEqualTo(outboxDo);
    AssertionsForClassTypes.assertThat(outboxWriteMongodbRepository.existsById(eventId)).isTrue();
  }
}
