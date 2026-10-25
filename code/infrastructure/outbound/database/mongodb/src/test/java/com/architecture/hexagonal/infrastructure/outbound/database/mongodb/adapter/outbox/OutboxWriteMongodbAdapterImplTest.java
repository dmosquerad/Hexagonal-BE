package com.architecture.hexagonal.infrastructure.outbound.database.mongodb.adapter.outbox;

import com.architecture.hexagonal.domain.model.entity.outbox.Outbox;
import com.architecture.hexagonal.domain.model.entity.user.User;
import com.architecture.hexagonal.domain.testutils.data.model.entity.outbox.OutboxTestDataBuilder;
import com.architecture.hexagonal.domain.testutils.data.model.entity.user.UserTestDataBuilder;
import com.architecture.hexagonal.domain.testutils.data.model.vo.payload.PayloadVoTestDataBuilder;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.data.outbox.OutboxDao;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.mapper.outbox.OutboxDaoMapper;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.mapper.outbox.OutboxDoFromMongodbMapper;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.repository.outbox.OutboxWriteMongodbRepository;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.testutils.data.outbox.OutboxDaoTestDataBuilder;
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
  void saveShouldPersistOutboxEventWhenEventIsValid() {
    final Outbox outbox =
        OutboxTestDataBuilder.<User>builder()
            .action("USER_CREATED")
            .payload(
                PayloadVoTestDataBuilder.<User>builder()
                    .data(UserTestDataBuilder.builder().build().user())
                    .build()
                    .payloadVo())
            .build()
            .outbox();
    final OutboxDao outboxDao = OutboxDaoTestDataBuilder.builder().build().outboxDao();

    Mockito.when(outboxWriteMongodbRepository.upsertByEventIdentity(Mockito.any(OutboxDao.class)))
        .thenReturn(outboxDao);

    final Outbox result = outboxWriteMongodbAdapterImpl.upsertByEventIdentity(outbox);

    AssertionsForClassTypes.assertThat(result).usingRecursiveComparison().isEqualTo(outbox);

    Mockito.verify(outboxDaoMapper).toOutboxDao(outbox);
    Mockito.verify(outboxWriteMongodbRepository)
        .upsertByEventIdentity(Mockito.any(OutboxDao.class));
    Mockito.verify(outboxDoFromMongodbMapper).toOutbox(outboxDao);
  }
}
