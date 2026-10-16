package com.architecture.hexagonal.infrastructure.outbound.database.mongodb.adapter.inbox;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.architecture.hexagonal.domain.model.entity.inbox.Inbox;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.data.inbox.InboxDao;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.mapper.inbox.InboxDaoMapper;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.mapper.inbox.InboxDoFromMongodbMapper;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.repository.inbox.InboxWriteMongodbRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
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
    final Inbox inbox = InboxRepositoryReadMongodbAdapterImplTest.inbox();
    final InboxDao inboxDao = Mappers.getMapper(InboxDaoMapper.class).toInboxDao(inbox);
    when(repository.save(any(InboxDao.class))).thenReturn(inboxDao);

    final Inbox result = adapter.save(inbox);

    assertThat(result).usingRecursiveComparison().isEqualTo(inbox);
    verify(inboxDaoMapper).toInboxDao(inbox);
    verify(repository).save(inboxDao);
    verify(inboxDoFromMongodbMapper).toInbox(inboxDao);
  }
}
