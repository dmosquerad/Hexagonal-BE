package com.architecture.hexagonal.infrastructure.outbound.database.mongodb.repository.inbox.custom.impl;

import com.architecture.hexagonal.domain.model.vo.payload.StatusVo;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.data.inbox.InboxDao;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.testutils.data.inbox.InboxDaoTestDataBuilder;
import org.assertj.core.api.Assertions;
import org.bson.Document;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.mongodb.core.ExecutableUpdateOperation.ExecutableUpdate;
import org.springframework.data.mongodb.core.ExecutableUpdateOperation.TerminatingFindAndModify;
import org.springframework.data.mongodb.core.ExecutableUpdateOperation.TerminatingUpdate;
import org.springframework.data.mongodb.core.ExecutableUpdateOperation.UpdateWithQuery;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

@ExtendWith(MockitoExtension.class)
class InboxWriteMongodbRepositoryImplTest {

  @InjectMocks private InboxWriteMongodbRepositoryImpl repository;

  @Mock private MongoTemplate mongoTemplate;
  @Mock private ExecutableUpdate executableUpdate;
  @Mock private UpdateWithQuery updateWithQuery;
  @Mock private TerminatingUpdate terminatingUpdate;
  @Mock private TerminatingFindAndModify terminatingFindAndModify;

  @Test
  void claimPendingForProcessingShouldUpdateAndReturnClaimedInbox() {
    final InboxDao inboxDao =
        InboxDaoTestDataBuilder.builder().retryCount(2).processedAt(null).build().inboxDao();
    final InboxDao claimedInbox =
        InboxDaoTestDataBuilder.builder()
            .retryCount(2)
            .status(StatusVo.PROCESSING)
            .processedAt(null)
            .build()
            .inboxDao();
    Mockito.when(mongoTemplate.update(InboxDao.class)).thenReturn(executableUpdate);
    Mockito.when(executableUpdate.matching(Mockito.any(Query.class))).thenReturn(updateWithQuery);
    Mockito.when(updateWithQuery.apply(Mockito.any(Update.class))).thenReturn(terminatingUpdate);
    Mockito.when(terminatingUpdate.withOptions(Mockito.any(FindAndModifyOptions.class)))
        .thenReturn(terminatingFindAndModify);
    Mockito.when(terminatingFindAndModify.findAndModifyValue()).thenReturn(claimedInbox);

    final InboxDao result = repository.claimPendingForProcessing(inboxDao);

    Assertions.assertThat(result).isSameAs(claimedInbox);
    final ArgumentCaptor<Query> queryCaptor = ArgumentCaptor.forClass(Query.class);
    final ArgumentCaptor<Update> updateCaptor = ArgumentCaptor.forClass(Update.class);
    Mockito.verify(executableUpdate).matching(queryCaptor.capture());
    Mockito.verify(updateWithQuery).apply(updateCaptor.capture());
    final Document query = queryCaptor.getValue().getQueryObject();
    Assertions.assertThat(query.get("payload.messageHeader.messageId"))
        .isEqualTo(inboxDao.getPayload().getMessageHeader().getMessageId());
    Assertions.assertThat(query.get("payload.messageHeader.messageDate"))
        .isEqualTo(inboxDao.getPayload().getMessageHeader().getMessageDate());
    Assertions.assertThat(query.get(InboxDao.Fields.status)).isEqualTo(StatusVo.PENDING);
    Assertions.assertThat(updateCaptor.getValue().getUpdateObject().get("$set", Document.class))
        .containsEntry(InboxDao.Fields.status, StatusVo.PROCESSING)
        .containsEntry(InboxDao.Fields.retryCount, inboxDao.getRetryCount())
        .containsEntry(InboxDao.Fields.processedAt, inboxDao.getProcessedAt());
    Mockito.verify(mongoTemplate).update(InboxDao.class);
    Mockito.verify(terminatingUpdate).withOptions(Mockito.any(FindAndModifyOptions.class));
    Mockito.verify(terminatingFindAndModify).findAndModifyValue();
  }

  @Test
  void updateByMessageIdentityShouldUpdateAndReturnInbox() {
    final InboxDao inboxDao =
        InboxDaoTestDataBuilder.builder()
            .status(StatusVo.PROCESSED)
            .retryCount(3)
            .build()
            .inboxDao();
    Mockito.when(mongoTemplate.update(InboxDao.class)).thenReturn(executableUpdate);
    Mockito.when(executableUpdate.matching(Mockito.any(Query.class))).thenReturn(updateWithQuery);
    Mockito.when(updateWithQuery.apply(Mockito.any(Update.class))).thenReturn(terminatingUpdate);
    Mockito.when(terminatingUpdate.withOptions(Mockito.any(FindAndModifyOptions.class)))
        .thenReturn(terminatingFindAndModify);
    Mockito.when(terminatingFindAndModify.findAndModifyValue()).thenReturn(inboxDao);

    final InboxDao result = repository.updateByMessageIdentity(inboxDao);

    Assertions.assertThat(result).isSameAs(inboxDao);
    final ArgumentCaptor<Query> queryCaptor = ArgumentCaptor.forClass(Query.class);
    final ArgumentCaptor<Update> updateCaptor = ArgumentCaptor.forClass(Update.class);
    Mockito.verify(executableUpdate).matching(queryCaptor.capture());
    Mockito.verify(updateWithQuery).apply(updateCaptor.capture());
    final Document query = queryCaptor.getValue().getQueryObject();
    Assertions.assertThat(query.get("payload.messageHeader.messageId"))
        .isEqualTo(inboxDao.getPayload().getMessageHeader().getMessageId());
    Assertions.assertThat(query.get("payload.messageHeader.messageDate"))
        .isEqualTo(inboxDao.getPayload().getMessageHeader().getMessageDate());
    Assertions.assertThat(query).doesNotContainKey(InboxDao.Fields.status);
    Assertions.assertThat(updateCaptor.getValue().getUpdateObject().get("$set", Document.class))
        .containsEntry(InboxDao.Fields.status, inboxDao.getStatus())
        .containsEntry(InboxDao.Fields.retryCount, inboxDao.getRetryCount())
        .containsEntry(InboxDao.Fields.processedAt, inboxDao.getProcessedAt());
    Mockito.verify(mongoTemplate).update(InboxDao.class);
    Mockito.verify(terminatingUpdate).withOptions(Mockito.any(FindAndModifyOptions.class));
    Mockito.verify(terminatingFindAndModify).findAndModifyValue();
  }

  @Test
  void updateByMessageIdentityShouldThrowWhenInboxIsNotFound() {
    final InboxDao inboxDao = InboxDaoTestDataBuilder.builder().build().inboxDao();
    Mockito.when(mongoTemplate.update(InboxDao.class)).thenReturn(executableUpdate);
    Mockito.when(executableUpdate.matching(Mockito.any(Query.class))).thenReturn(updateWithQuery);
    Mockito.when(updateWithQuery.apply(Mockito.any(Update.class))).thenReturn(terminatingUpdate);
    Mockito.when(terminatingUpdate.withOptions(Mockito.any(FindAndModifyOptions.class)))
        .thenReturn(terminatingFindAndModify);
    Mockito.when(terminatingFindAndModify.findAndModifyValue()).thenReturn(null);

    Assertions.assertThatThrownBy(() -> repository.updateByMessageIdentity(inboxDao))
        .isInstanceOf(IllegalStateException.class)
        .hasMessageContaining(inboxDao.getPayload().getMessageHeader().getMessageId().toString());
    Mockito.verify(mongoTemplate).update(InboxDao.class);
    Mockito.verify(terminatingUpdate).withOptions(Mockito.any(FindAndModifyOptions.class));
    Mockito.verify(terminatingFindAndModify).findAndModifyValue();
  }
}
