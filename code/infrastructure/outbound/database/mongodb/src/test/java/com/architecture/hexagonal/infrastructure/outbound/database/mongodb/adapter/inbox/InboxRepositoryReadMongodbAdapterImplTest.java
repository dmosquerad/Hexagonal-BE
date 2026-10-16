package com.architecture.hexagonal.infrastructure.outbound.database.mongodb.adapter.inbox;

import static org.assertj.core.api.Assertions.assertThat;

import com.architecture.hexagonal.domain.model.entity.inbox.Inbox;
import com.architecture.hexagonal.domain.model.vo.payload.AggregateTypeVo;
import com.architecture.hexagonal.domain.model.vo.payload.MessageHeaderVo;
import com.architecture.hexagonal.domain.model.vo.payload.PayloadVo;
import com.architecture.hexagonal.domain.model.vo.payload.StatusVo;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.data.inbox.InboxDao;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.mapper.inbox.InboxDaoMapper;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.mapper.inbox.InboxDoFromMongodbMapper;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.repository.inbox.InboxReadMongodbRepository;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
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
    final Inbox inbox = inbox();
    final InboxDao inboxDao = Mappers.getMapper(InboxDaoMapper.class).toInboxDao(inbox);
    org.mockito.Mockito.when(
            repository.findByStatusAndAggregateTypeOrderByCreatedAtAsc(
                StatusVo.PENDING, inbox.aggregateType()))
        .thenReturn(List.of(inboxDao));

    final List<Inbox> result =
        adapter.findByStatusAndAggregateType(StatusVo.PENDING, inbox.aggregateType());

    assertThat(result).usingRecursiveComparison().isEqualTo(List.of(inbox));
    org.mockito.Mockito.verify(repository)
        .findByStatusAndAggregateTypeOrderByCreatedAtAsc(StatusVo.PENDING, inbox.aggregateType());
    org.mockito.Mockito.verify(inboxDoFromMongodbMapper).toInbox(inboxDao);
  }

  static Inbox inbox() {
    final OffsetDateTime createdAt = OffsetDateTime.parse("2026-10-13T12:15:30.123+02:00");
    return Inbox.builder()
        .payload(
            PayloadVo.builder()
                .messageHeader(
                    MessageHeaderVo.builder()
                        .messageId(UUID.fromString("b45f40b7-1b2b-4f4f-9f80-636bb1150c47"))
                        .messageDate(createdAt)
                        .build())
                .data(Map.of("name", "Test User"))
                .build())
        .aggregateType(AggregateTypeVo.USER)
        .aggregateId("user-123")
        .action("USER_CREATED")
        .status(StatusVo.PENDING)
        .retryCount(0)
        .createdAt(createdAt)
        .processedAt(null)
        .build();
  }
}
