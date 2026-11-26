package com.architecture.hexagonal.infrastructure.outbound.database.mongodb.adapter.outbox;

import com.architecture.hexagonal.domain.databuilder.model.criteria.outbox.OutboxStatusAndAggregateCriteriaTestDataBuilder;
import com.architecture.hexagonal.domain.databuilder.model.entity.outbox.OutboxTestDataBuilder;
import com.architecture.hexagonal.domain.databuilder.model.entity.user.UserTestDataBuilder;
import com.architecture.hexagonal.domain.databuilder.model.vo.payload.PayloadVoTestDataBuilder;
import com.architecture.hexagonal.domain.model.criteria.outbox.OutboxStatusAndAggregateCriteria;
import com.architecture.hexagonal.domain.model.entity.outbox.Outbox;
import com.architecture.hexagonal.domain.model.entity.user.User;
import com.architecture.hexagonal.domain.model.vo.payload.ActionType;
import com.architecture.hexagonal.domain.model.vo.payload.StatusVo;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.data.outbox.OutboxDao;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.databuilder.data.outbox.OutboxDaoTestDataBuilder;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.mapper.outbox.OutboxDoFromMongodbMapper;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.repository.outbox.OutboxReadMongodbRepository;
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
            .action(ActionType.UserActionType.USER_CREATED.getActionType())
            .payload(
                PayloadVoTestDataBuilder.<User>builder()
                    .data(UserTestDataBuilder.builder().build().user())
                    .build()
                    .payloadVo())
            .build()
            .outbox();
    final OutboxStatusAndAggregateCriteria outboxStatusAndAggregateCriteria =
        OutboxStatusAndAggregateCriteriaTestDataBuilder.builder()
            .build()
            .outboxStatusAndAggregateCriteria();
    final OutboxDao outboxDao = OutboxDaoTestDataBuilder.builder().build().outboxDao();

    Mockito.when(
            outboxReadMongodbRepository.findByStatusAndAggregateTypeOrderByCreatedAtAsc(
                StatusVo.PENDING, outbox.aggregateType()))
        .thenReturn(List.of(outboxDao));

    final List<Outbox> result =
        outboxReadMongodbAdapterImpl.findByStatusAndAggregateType(outboxStatusAndAggregateCriteria);

    AssertionsForClassTypes.assertThat(result)
        .usingRecursiveComparison()
        .isEqualTo(List.of(outbox));

    Mockito.verify(outboxReadMongodbRepository)
        .findByStatusAndAggregateTypeOrderByCreatedAtAsc(StatusVo.PENDING, outbox.aggregateType());
    Mockito.verify(outboxDoFromMongodbMapper).toOutbox(Mockito.any(OutboxDao.class));
  }
}
