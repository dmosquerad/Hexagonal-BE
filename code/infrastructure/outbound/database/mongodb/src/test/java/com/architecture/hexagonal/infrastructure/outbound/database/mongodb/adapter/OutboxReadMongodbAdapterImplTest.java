package com.architecture.hexagonal.infrastructure.outbound.database.mongodb.adapter;

import com.architecture.hexagonal.domain.model.entity.OutboxDo;
import com.architecture.hexagonal.domain.model.vo.OutboxStatusVo;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.data.OutboxDao;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.mapper.outbox.OutboxDoFromMongodbMapper;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.repository.OutboxReadMongodbRepository;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.testutils.data.OutboxEventDaoTestDataBuilder;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.testutils.data.OutboxEventDoTestDataBuilder;
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
    final OutboxDo outboxDo = OutboxEventDoTestDataBuilder.builder().build().outboxEventDo();
    final OutboxDao outboxDao = OutboxEventDaoTestDataBuilder.builder().build().outboxEventDao();

    Mockito.when(
            outboxReadMongodbRepository.findByStatusOrderByCreatedAtAsc(OutboxStatusVo.PENDING))
        .thenReturn(List.of(outboxDao));

    final List<OutboxDo> result = outboxReadMongodbAdapterImpl.findPendingEvents();

    AssertionsForClassTypes.assertThat(result)
        .usingRecursiveComparison()
        .isEqualTo(List.of(outboxDo));

    Mockito.verify(outboxReadMongodbRepository)
        .findByStatusOrderByCreatedAtAsc(OutboxStatusVo.PENDING);
    Mockito.verify(outboxDoFromMongodbMapper).toOutboxEvent(ArgumentMatchers.any(OutboxDao.class));
  }
}
