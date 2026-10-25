package com.architecture.hexagonal.infrastructure.outbound.database.mongodb.adapter.inbox;

import com.architecture.hexagonal.domain.model.entity.inbox.Inbox;
import com.architecture.hexagonal.domain.model.vo.payload.StatusVo;
import com.architecture.hexagonal.domain.testutils.data.model.entity.inbox.InboxTestDataBuilder;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.data.inbox.InboxDao;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.mapper.inbox.InboxDoFromMongodbMapper;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.repository.inbox.InboxReadMongodbRepository;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.testutils.data.inbox.InboxDaoTestDataBuilder;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class InboxRepositoryReadMongodbAdapterImplTest {

  @InjectMocks private InboxRepositoryReadMongodbAdapterImpl adapter;

  @Mock private InboxReadMongodbRepository repository;

  @Spy
  private InboxDoFromMongodbMapper inboxDoFromMongodbMapper =
      Mappers.getMapper(InboxDoFromMongodbMapper.class);

  @Test
  void findByStatusShouldMapDocumentsToInboxEntities() {
    final Inbox inbox = InboxTestDataBuilder.builder().build().inbox();
    final InboxDao inboxDao = InboxDaoTestDataBuilder.builder().build().inboxDao();
    Mockito.when(
            repository.findByStatusAndAggregateTypeOrderByCreatedAtAsc(
                StatusVo.PENDING, inbox.aggregateType()))
        .thenReturn(List.of(inboxDao));

    final List<Inbox> result =
        adapter.findByStatusAndAggregateType(StatusVo.PENDING, inbox.aggregateType());

    Assertions.assertThat(result).usingRecursiveComparison().isEqualTo(List.of(inbox));
    Mockito.verify(repository)
        .findByStatusAndAggregateTypeOrderByCreatedAtAsc(StatusVo.PENDING, inbox.aggregateType());
    Mockito.verify(inboxDoFromMongodbMapper).toInbox(inboxDao);
  }
}
