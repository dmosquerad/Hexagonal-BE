package com.architecture.hexagonal.infrastructure.outbound.database.mongodb.adapter.outbox;

import com.architecture.hexagonal.application.projector.outbox.OutboxStatusAndAggregateProjector;
import com.architecture.hexagonal.application.testutils.projector.outbox.OutboxStatusAndAggregateProjectorTestDataBuilder;
import com.architecture.hexagonal.domain.model.entity.outbox.Outbox;
import com.architecture.hexagonal.domain.model.entity.user.User;
import com.architecture.hexagonal.domain.model.vo.payload.StatusVo;
import com.architecture.hexagonal.domain.testutils.data.model.entity.outbox.OutboxTestDataBuilder;
import com.architecture.hexagonal.domain.testutils.data.model.entity.user.UserTestDataBuilder;
import com.architecture.hexagonal.domain.testutils.data.model.vo.payload.PayloadVoTestDataBuilder;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.data.outbox.OutboxDao;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.mapper.outbox.OutboxDoFromMongodbMapper;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.repository.outbox.OutboxReadMongodbRepository;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.testutils.data.outbox.OutboxDaoTestDataBuilder;
import java.util.List;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
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
  void findPendingEventsShouldReturnPendingEventsOrderedByCreatedAtWhenPendingEventsExist() {
    final Outbox outbox =
        OutboxTestDataBuilder.<User>builder()
            .payload(
                PayloadVoTestDataBuilder.<User>builder()
                    .data(UserTestDataBuilder.builder().build().user())
                    .build()
                    .payloadVo())
            .build()
            .outbox();
    final OutboxStatusAndAggregateProjector outboxStatusAndAggregateProjector =
        OutboxStatusAndAggregateProjectorTestDataBuilder.builder()
            .build()
            .outboxStatusAndAggregateProjector();
    final OutboxDao outboxDao = OutboxDaoTestDataBuilder.builder().build().outboxEventDao();

    Mockito.when(
            outboxReadMongodbRepository.findByStatusAndAggregateTypeOrderByCreatedAtAsc(
                StatusVo.PENDING, outbox.aggregateType()))
        .thenReturn(List.of(outboxDao));

    final List<Outbox> result =
        outboxReadMongodbAdapterImpl.findByStatusAndAggregateType(
            outboxStatusAndAggregateProjector);

    AssertionsForClassTypes.assertThat(result)
        .usingRecursiveComparison()
        .isEqualTo(List.of(outbox));

    Mockito.verify(outboxReadMongodbRepository)
        .findByStatusAndAggregateTypeOrderByCreatedAtAsc(StatusVo.PENDING, outbox.aggregateType());
    Mockito.verify(outboxDoFromMongodbMapper).toOutbox(Mockito.any(OutboxDao.class));
  }
}
