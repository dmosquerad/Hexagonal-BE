package com.architecture.hexagonal.infrastructure.outbound.database.mongodb.adapter;

import com.architecture.hexagonal.domain.model.entity.outbox.Outbox;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.data.OutboxDao;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.mapper.outbox.OutboxDaoMapper;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.mapper.outbox.OutboxDoFromMongodbMapper;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.repository.OutboxWriteMongodbRepository;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.testutils.dao.OutboxDaoTestDataBuilder;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.testutils.model.entity.outbox.OutboxTestDataBuilder;
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
class OutboxWriteMongodbAdapterImplTest {

  @InjectMocks OutboxRepositoryWriteMongodbAdapterImpl outboxWriteMongodbAdapterImpl;

  @Mock OutboxWriteMongodbRepository outboxWriteMongodbRepository;

  @Spy
  OutboxDoFromMongodbMapper outboxDoFromMongodbMapper =
      Mappers.getMapper(OutboxDoFromMongodbMapper.class);

  @Spy OutboxDaoMapper outboxDaoMapper = Mappers.getMapper(OutboxDaoMapper.class);

  @Test
  void save_shouldPersistOutboxEvent_whenEventIsValid() {
    final Outbox outbox = OutboxTestDataBuilder.builder().processedAt(null).build().outboxEventDo();
    final OutboxDao outboxDao =
        OutboxDaoTestDataBuilder.builder().processedAt(null).build().outboxEventDao();

    Mockito.when(outboxWriteMongodbRepository.findAndSaveWithMerge(Mockito.any(OutboxDao.class)))
        .thenReturn(outboxDao);

    final Outbox result = outboxWriteMongodbAdapterImpl.save(outbox);

    AssertionsForClassTypes.assertThat(result).usingRecursiveComparison().isEqualTo(outbox);

    Mockito.verify(outboxDaoMapper).toOutboxEventDao(outbox);
    Mockito.verify(outboxWriteMongodbRepository).findAndSaveWithMerge(Mockito.any(OutboxDao.class));
    Mockito.verify(outboxDoFromMongodbMapper).toOutboxEvent(outboxDao);
  }
}
