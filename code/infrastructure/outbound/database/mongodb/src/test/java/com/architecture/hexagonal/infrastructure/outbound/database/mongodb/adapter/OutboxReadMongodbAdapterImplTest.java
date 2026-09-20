package com.architecture.hexagonal.infrastructure.outbound.database.mongodb.adapter;

import com.architecture.hexagonal.application.usecase.technical.outbox.find.projector.OutboxStatusAndAggregateProjector;
import com.architecture.hexagonal.domain.model.entity.outbox.Outbox;
import com.architecture.hexagonal.domain.model.vo.outbox.OutboxStatusVo;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.data.OutboxDao;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.mapper.outbox.OutboxDoFromMongodbMapper;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.repository.OutboxReadMongodbRepository;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.testutils.dao.OutboxDaoTestDataBuilder;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.testutils.data.aggregate.OutboxStatusAndAggregateProjectorTestDataBuilder;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.testutils.data.aggregate.OutboxTestDataBuilder;
import java.util.List;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OutboxReadMongodbAdapterImplTest {

  @InjectMocks private OutboxRepositoryReadMongodbAdapterImpl outboxReadMongodbAdapterImpl;

  @Mock private OutboxReadMongodbRepository outboxReadMongodbRepository;

  @Spy
  private OutboxDoFromMongodbMapper outboxDoFromMongodbMapper =
      Mappers.getMapper(OutboxDoFromMongodbMapper.class);

  @Test
  void findPendingEvents_shouldReturnPendingEventsOrderedByCreatedAt_whenPendingEventsExist() {
    final Outbox outbox = OutboxTestDataBuilder.builder().build().outboxEventDo();
    final OutboxStatusAndAggregateProjector outboxStatusAndAggregateProjector =
        OutboxStatusAndAggregateProjectorTestDataBuilder.builder()
            .build()
            .outboxStatusAndAggregateProjector();
    final OutboxDao outboxDao = OutboxDaoTestDataBuilder.builder().build().outboxEventDao();

    Mockito.when(
            outboxReadMongodbRepository.findByStatusAndAggregateTypeOrderByCreatedAtAsc(
                OutboxStatusVo.PENDING, outbox.aggregateType()))
        .thenReturn(List.of(outboxDao));

    final List<Outbox> result =
        outboxReadMongodbAdapterImpl.findByStatusAndAggregateType(
            outboxStatusAndAggregateProjector);

    AssertionsForClassTypes.assertThat(result)
        .usingRecursiveComparison()
        .isEqualTo(List.of(outbox));

    Mockito.verify(outboxReadMongodbRepository)
        .findByStatusAndAggregateTypeOrderByCreatedAtAsc(
            OutboxStatusVo.PENDING, outbox.aggregateType());
    Mockito.verify(outboxDoFromMongodbMapper).toOutboxEvent(ArgumentMatchers.any(OutboxDao.class));
  }
}
