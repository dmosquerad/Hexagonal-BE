package com.architecture.hexagonal.infrastructure.outbound.database.mongodb.adapter.outbox;

import com.architecture.hexagonal.domain.model.entity.outbox.Outbox;
import com.architecture.hexagonal.domain.model.entity.user.User;
import com.architecture.hexagonal.application.projector.outbox.OutboxStatusAndAggregateProjector;
import com.architecture.hexagonal.application.testutils.projector.outbox.OutboxStatusAndAggregateProjectorTestDataBuilder;
import com.architecture.hexagonal.domain.testutils.data.model.entity.outbox.OutboxTestDataBuilder;
import com.architecture.hexagonal.domain.testutils.data.model.entity.user.UserTestDataBuilder;
import com.architecture.hexagonal.domain.testutils.data.model.vo.payload.PayloadVoTestDataBuilder;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.config.MongodbIT;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.config.MongodbTestApplication;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.data.outbox.OutboxDao;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.mapper.outbox.OutboxDoFromMongodbMapper;

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
  void findPendingEventsShouldReturnPendingEventsOrderedByCreatedAt() {
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
    final OutboxStatusAndAggregateProjector outboxStatusAndAggregateProjector = OutboxStatusAndAggregateProjectorTestDataBuilder.builder().build().outboxStatusAndAggregateProjector();

    final List<Outbox> result = outboxReadMongodbAdapterImpl.findByStatusAndAggregateType(outboxStatusAndAggregateProjector);

    Mockito.verify(outboxDoFromMongodbMapper).toOutbox(Mockito.any(OutboxDao.class));

    AssertionsForClassTypes.assertThat(result)
        .usingRecursiveComparison()
        .isEqualTo(List.of(outbox));
  }
}
