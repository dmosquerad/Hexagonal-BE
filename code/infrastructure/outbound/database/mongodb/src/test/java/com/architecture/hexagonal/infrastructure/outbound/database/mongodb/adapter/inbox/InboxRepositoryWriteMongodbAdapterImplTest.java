package com.architecture.hexagonal.infrastructure.outbound.database.mongodb.adapter.inbox;

import com.architecture.hexagonal.domain.model.entity.inbox.Inbox;
import com.architecture.hexagonal.domain.testutils.data.model.entity.inbox.InboxTestDataBuilder;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.data.inbox.InboxDao;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.mapper.inbox.InboxDaoMapper;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.mapper.inbox.InboxDoFromMongodbMapper;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.repository.inbox.InboxWriteMongodbRepository;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.testutils.data.inbox.InboxDaoTestDataBuilder;
import org.assertj.core.api.Assertions;
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
class InboxRepositoryWriteMongodbAdapterImplTest {

  @InjectMocks private InboxRepositoryWriteMongodbAdapterImpl adapter;

  @Mock private InboxWriteMongodbRepository repository;

  @Spy private InboxDaoMapper inboxDaoMapper = Mappers.getMapper(InboxDaoMapper.class);

  @Spy
  private InboxDoFromMongodbMapper inboxDoFromMongodbMapper =
      Mappers.getMapper(InboxDoFromMongodbMapper.class);

  @Test
  void saveShouldMapInboxToDocumentAndBack() {
    final Inbox inbox = InboxTestDataBuilder.builder().build().inbox();
    final InboxDao inboxDao = InboxDaoTestDataBuilder.builder().build().inboxDao();

    Mockito.when(repository.save(ArgumentMatchers.any(InboxDao.class))).thenReturn(inboxDao);

    final Inbox result = adapter.save(inbox);

    Assertions.assertThat(result).usingRecursiveComparison().isEqualTo(inbox);
    Mockito.verify(inboxDaoMapper).toInboxDao(inbox);
    Mockito.verify(repository).save(inboxDao);
    Mockito.verify(inboxDoFromMongodbMapper).toInbox(inboxDao);
  }
}
