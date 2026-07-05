package com.architecture.hexagonal.infrastructure.outbound.database.mongodb.adapter;

import com.architecture.hexagonal.domain.model.entity.OutboxDo;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.config.MongodbIT;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.config.MongodbTestApplication;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.testutils.data.OutboxEventDoTestDataBuilder;

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
    final OutboxDo outboxDo = OutboxEventDoTestDataBuilder.builder().processedAt(null).build().outboxEventDo();

    final List<OutboxDo> result = outboxReadMongodbAdapterImpl.findPendingEvents();

    AssertionsForClassTypes.assertThat(result)
        .usingRecursiveComparison()
        .isEqualTo(List.of(outboxDo));
  }
}
