package com.architecture.hexagonal.infrastructure.outbound.database.mongodb.repository.custom.impl;

import com.architecture.hexagonal.domain.model.vo.outbox.OutboxStatusVo;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.data.OutboxDao;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.testutils.dao.OutboxDaoTestDataBuilder;
import java.time.OffsetDateTime;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.mongodb.core.ExecutableUpdateOperation.ExecutableUpdate;
import org.springframework.data.mongodb.core.ExecutableUpdateOperation.UpdateWithQuery;
import org.springframework.data.mongodb.core.ExecutableUpdateOperation.TerminatingUpdate;
import org.springframework.data.mongodb.core.ExecutableUpdateOperation.TerminatingFindAndModify;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

@ExtendWith(MockitoExtension.class)
class OutboxWriteMongodbRepositoryImplTest {

  @InjectMocks private OutboxWriteMongodbRepositoryImpl outboxWriteMongodbRepositoryImpl;

  @Mock private MongoTemplate mongoTemplate;

  @Mock private ExecutableUpdate executableUpdate;

  @Mock private UpdateWithQuery updateWithQuery;

  @Mock private TerminatingUpdate terminatingUpdate;

  @Mock private TerminatingFindAndModify terminatingFindAndModify;

  @Test
  void findAndSaveWithMerge_shouldUpdateStatusAndUpsert_whenDocumentExists() {
    final OutboxDao outboxDao =
        OutboxDaoTestDataBuilder.builder().processedAt(null).build().outboxEventDao();
    final OutboxDao persistedDao =
        OutboxDaoTestDataBuilder.builder().processedAt(null).build().outboxEventDao();

    Mockito.when(mongoTemplate.update(OutboxDao.class)).thenReturn(executableUpdate);
    Mockito.when(executableUpdate.matching(Mockito.any(Query.class))).thenReturn(updateWithQuery);
    Mockito.when(updateWithQuery.apply(Mockito.any(Update.class))).thenReturn(terminatingUpdate);
    Mockito.when(terminatingUpdate.withOptions(Mockito.any(FindAndModifyOptions.class)))
      .thenReturn(terminatingFindAndModify);
    Mockito.when(terminatingFindAndModify.findAndModifyValue()).thenReturn(outboxDao);

    final OutboxDao result = outboxWriteMongodbRepositoryImpl.findAndSaveWithMerge(outboxDao);

    AssertionsForClassTypes.assertThat(result).usingRecursiveComparison().isEqualTo(persistedDao);
    
    Mockito.verify(mongoTemplate).update(OutboxDao.class);
    Mockito.verify(executableUpdate).matching(Mockito.any(Query.class));
    Mockito.verify(updateWithQuery).apply(Mockito.any(Update.class));
    Mockito.verify(terminatingUpdate).withOptions(Mockito.any(FindAndModifyOptions.class));
    Mockito.verify(terminatingFindAndModify).findAndModifyValue();
  }

  @Test
  void findAndSaveWithMerge_shouldSetRetryCount_whenRetryCountGreaterThanZero() {
    final OutboxDao outboxDao =
        OutboxDaoTestDataBuilder.builder().processedAt(null).retryCount(3).build().outboxEventDao();

    Mockito.when(mongoTemplate.update(OutboxDao.class)).thenReturn(executableUpdate);
    Mockito.when(executableUpdate.matching(Mockito.any(Query.class))).thenReturn(updateWithQuery);
    Mockito.when(updateWithQuery.apply(Mockito.any(Update.class))).thenReturn(terminatingUpdate);
    Mockito.when(terminatingUpdate.withOptions(Mockito.any(FindAndModifyOptions.class)))
      .thenReturn(terminatingFindAndModify);
    Mockito.when(terminatingFindAndModify.findAndModifyValue()).thenReturn(outboxDao);

    final OutboxDao result = outboxWriteMongodbRepositoryImpl.findAndSaveWithMerge(outboxDao);

    AssertionsForClassTypes.assertThat(result).usingRecursiveComparison().isEqualTo(outboxDao);
    
    Mockito.verify(mongoTemplate).update(OutboxDao.class);
    Mockito.verify(executableUpdate).matching(Mockito.any(Query.class));
    Mockito.verify(updateWithQuery).apply(Mockito.any(Update.class));
    Mockito.verify(terminatingUpdate).withOptions(Mockito.any(FindAndModifyOptions.class));
    Mockito.verify(terminatingFindAndModify).findAndModifyValue();
  }

  @Test
  void findAndSaveWithMerge_shouldSetProcessedAt_whenProcessedAtIsNotNull() {
    final OffsetDateTime processedAt = OffsetDateTime.now();
    final OutboxDao outboxDao =
        OutboxDaoTestDataBuilder.builder()
            .status(OutboxStatusVo.PUBLISHED)
            .processedAt(processedAt)
            .build()
            .outboxEventDao();

    Mockito.when(mongoTemplate.update(OutboxDao.class)).thenReturn(executableUpdate);
    Mockito.when(executableUpdate.matching(Mockito.any(Query.class))).thenReturn(updateWithQuery);
    Mockito.when(updateWithQuery.apply(Mockito.any(Update.class))).thenReturn(terminatingUpdate);
    Mockito.when(terminatingUpdate.withOptions(Mockito.any(FindAndModifyOptions.class)))
      .thenReturn(terminatingFindAndModify);
    Mockito.when(terminatingFindAndModify.findAndModifyValue()).thenReturn(outboxDao);

    final OutboxDao result = outboxWriteMongodbRepositoryImpl.findAndSaveWithMerge(outboxDao);

    AssertionsForClassTypes.assertThat(result).usingRecursiveComparison().isEqualTo(outboxDao);
    
    Mockito.verify(mongoTemplate).update(OutboxDao.class);
    Mockito.verify(executableUpdate).matching(Mockito.any(Query.class));
    Mockito.verify(updateWithQuery).apply(Mockito.any(Update.class));
    Mockito.verify(terminatingUpdate).withOptions(Mockito.any(FindAndModifyOptions.class));
    Mockito.verify(terminatingFindAndModify).findAndModifyValue();
  }
}
