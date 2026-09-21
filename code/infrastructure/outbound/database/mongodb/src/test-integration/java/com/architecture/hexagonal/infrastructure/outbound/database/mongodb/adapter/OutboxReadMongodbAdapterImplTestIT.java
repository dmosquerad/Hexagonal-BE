package com.architecture.hexagonal.infrastructure.outbound.database.mongodb.adapter;

import com.architecture.hexagonal.application.usecase.technical.outbox.find.projector.OutboxStatusAndAggregateProjector;
import com.architecture.hexagonal.domain.model.entity.outbox.Outbox;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.config.MongodbIT;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.config.MongodbTestApplication;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.data.OutboxDao;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.mapper.outbox.OutboxDoFromMongodbMapper;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.testutils.usecase.technical.outbox.find.projector.OutboxStatusAndAggregateProjectorTestDataBuilder;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.testutils.model.entity.outbox.OutboxTestDataBuilder;

import java.util.List;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

@SpringBootTest(classes = {OutboxRepositoryReadMongodbAdapterImpl.class})
@ContextConfiguration(classes = MongodbTestApplication.class)
class OutboxReadMongodbAdapterImplTestIT extends MongodbIT {

  @Autowired private OutboxRepositoryReadMongodbAdapterImpl outboxReadMongodbAdapterImpl;

  @MockitoSpyBean private OutboxDoFromMongodbMapper outboxDoFromMongodbMapper;

  @Test
  void findPendingEvents_shouldReturnPendingEventsOrderedByCreatedAt() {
    final Outbox outbox = OutboxTestDataBuilder.builder().processedAt(null).build().outboxEventDo();
    final OutboxStatusAndAggregateProjector outboxStatusAndAggregateProjector = OutboxStatusAndAggregateProjectorTestDataBuilder.builder().build().outboxStatusAndAggregateProjector();

    final List<Outbox> result = outboxReadMongodbAdapterImpl.findByStatusAndAggregateType(outboxStatusAndAggregateProjector);

    Mockito.verify(outboxDoFromMongodbMapper).toOutboxEvent(Mockito.any(OutboxDao.class));

    AssertionsForClassTypes.assertThat(result)
        .usingRecursiveComparison()
        .isEqualTo(List.of(outbox));
  }
}
