package com.architecture.hexagonal.infrastructure.outbound.database.mongodb.integration.adapter.outbox;

import com.architecture.hexagonal.domain.databuilder.model.entity.outbox.OutboxTestDataBuilder;
import com.architecture.hexagonal.domain.databuilder.model.entity.user.UserTestDataBuilder;
import com.architecture.hexagonal.domain.databuilder.model.criteria.outbox.OutboxStatusAndAggregateCriteriaTestDataBuilder;
import com.architecture.hexagonal.domain.databuilder.model.vo.payload.PayloadVoTestDataBuilder;
import com.architecture.hexagonal.domain.model.entity.outbox.Outbox;
import com.architecture.hexagonal.domain.model.entity.user.User;
import com.architecture.hexagonal.domain.model.criteria.outbox.OutboxStatusAndAggregateCriteria;
import com.architecture.hexagonal.domain.model.vo.payload.ActionType;
import com.architecture.hexagonal.domain.model.vo.time.DateVo;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.adapter.outbox.OutboxRepositoryReadMongodbAdapterImpl;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.config.MongodbIT;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.config.MongodbTestApplication;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.data.outbox.OutboxDao;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.mapper.outbox.OutboxDaoMapper;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.mapper.outbox.OutboxDoFromMongodbMapper;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.repository.outbox.OutboxWriteMongodbRepository;
import java.util.List;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

@SpringBootTest(classes = {OutboxRepositoryReadMongodbAdapterImpl.class})
@ContextConfiguration(classes = MongodbTestApplication.class)
class OutboxRepositoryReadMongodbAdapterImplTestIT extends MongodbIT {

  @Autowired private OutboxRepositoryReadMongodbAdapterImpl outboxReadMongodbAdapterImpl;

  @Autowired private OutboxWriteMongodbRepository outboxWriteMongodbRepository;

  @MockitoSpyBean private OutboxDaoMapper outboxDaoMapper;

  @MockitoSpyBean private OutboxDoFromMongodbMapper outboxDoFromMongodbMapper;

  @BeforeEach
  void cleanOutboxCollection() {
    outboxWriteMongodbRepository.deleteAll();
  }

  @Test
  void findPendingEventsShouldReturnPendingEventsOrderedByCreatedAt() {
    final Outbox outbox =
        OutboxTestDataBuilder.<User>builder()
            .action(ActionType.UserActionType.USER_CREATED.getActionType())
            .payload(
                PayloadVoTestDataBuilder.<User>builder()
                    .data(UserTestDataBuilder.builder().build().user())
                    .build()
                    .payloadVo())
            .processedAt(null)
            .build()
            .outbox();
    outboxWriteMongodbRepository.save(outboxDaoMapper.toOutboxDao(outbox));

    final OutboxStatusAndAggregateCriteria criteria =
        OutboxStatusAndAggregateCriteriaTestDataBuilder.builder()
            .build()
            .outboxStatusAndAggregateCriteria();
    final List<Outbox> result =
        outboxReadMongodbAdapterImpl.findByStatusAndAggregateType(criteria);

    Mockito.verify(outboxDoFromMongodbMapper).toOutbox(Mockito.any(OutboxDao.class));

    AssertionsForClassTypes.assertThat(result)
        .usingRecursiveComparison()
        .isEqualTo(List.of(outbox.toBuilder().processedAt(DateVo.builder().build()).build()));
  }
}
