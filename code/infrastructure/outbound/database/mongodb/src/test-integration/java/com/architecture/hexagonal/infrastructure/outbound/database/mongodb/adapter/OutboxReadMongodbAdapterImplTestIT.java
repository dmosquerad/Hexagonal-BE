package com.architecture.hexagonal.infrastructure.outbound.database.mongodb.adapter;

import com.architecture.hexagonal.domain.model.aggregate.outbox.Outbox;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.config.MongodbIT;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.config.MongodbTestApplication;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.testutils.data.aggregate.OutboxTestDataBuilder;

import java.util.List;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest(classes = {OutboxRepositoryReadMongodbAdapterImpl.class})
@ContextConfiguration(classes = MongodbTestApplication.class)
class OutboxReadMongodbAdapterImplTestIT extends MongodbIT {

  @Autowired private OutboxRepositoryReadMongodbAdapterImpl outboxReadMongodbAdapterImpl;

  @Test
  void findPendingEvents_shouldReturnPendingEventsOrderedByCreatedAt() {
    final Outbox outbox = OutboxTestDataBuilder.builder().processedAt(null).build().outboxEventDo();

    final List<Outbox> result = outboxReadMongodbAdapterImpl.findPendingEvents();

    AssertionsForClassTypes.assertThat(result)
        .usingRecursiveComparison()
        .isEqualTo(List.of(outbox));
  }
}
