package com.architecture.hexagonal.infrastructure.outbound.database.mongodb.adapter.inbox;

import com.architecture.hexagonal.domain.model.entity.inbox.Inbox;
import com.architecture.hexagonal.domain.model.vo.payload.AggregateTypeVo;
import com.architecture.hexagonal.domain.model.vo.payload.MessageHeaderVo;
import com.architecture.hexagonal.domain.model.vo.payload.PayloadVo;
import com.architecture.hexagonal.domain.model.vo.payload.StatusVo;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.config.MongodbIT;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.config.MongodbTestApplication;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.data.inbox.InboxDao;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.data.payload.MessageHeaderDao;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.data.payload.PayloadDao;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.repository.inbox.InboxReadMongodbRepository;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.repository.inbox.InboxWriteMongodbRepository;
import java.time.OffsetDateTime;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest(classes = {InboxRepositoryReadMongodbAdapterImpl.class})
@ContextConfiguration(classes = MongodbTestApplication.class)
class InboxReadMongodbAdapterImplTestIT extends MongodbIT {

  @Autowired private InboxRepositoryReadMongodbAdapterImpl inboxReadMongodbAdapter;
  @Autowired private InboxReadMongodbRepository inboxReadMongodbRepository;
  @Autowired private InboxWriteMongodbRepository inboxWriteMongodbRepository;

  @BeforeEach
  void cleanInbox() {
    inboxWriteMongodbRepository.deleteAll();
  }

  @Test
  void findByStatusAndAggregateTypeShouldReturnPendingInboxOrderedByCreatedAt() {
    final Inbox oldest =
        inbox(
            UUID.randomUUID(),
            OffsetDateTime.parse("2026-10-13T10:15:30Z"),
            StatusVo.PENDING);
    final Inbox newest =
        inbox(
            UUID.randomUUID(),
            OffsetDateTime.parse("2026-10-13T10:17:30Z"),
            StatusVo.PENDING);
    final Inbox processed =
        inbox(
            UUID.randomUUID(),
            OffsetDateTime.parse("2026-10-13T10:16:30Z"),
            StatusVo.PROCESSED);
    inboxWriteMongodbRepository.save(toDao(newest));
    inboxWriteMongodbRepository.save(toDao(processed));
    inboxWriteMongodbRepository.save(toDao(oldest));

    final List<Inbox> result =
        inboxReadMongodbAdapter.findByStatusAndAggregateType(
            StatusVo.PENDING, AggregateTypeVo.USER);

    AssertionsForClassTypes.assertThat(result)
        .usingRecursiveComparison()
        .isEqualTo(List.of(oldest, newest));
    AssertionsForClassTypes.assertThat(inboxReadMongodbRepository.count()).isEqualTo(3);
  }

  private Inbox inbox(
      final UUID messageId, final OffsetDateTime createdAt, final StatusVo status) {
    return Inbox.builder()
        .payload(
            PayloadVo.builder()
                .messageHeader(
                    MessageHeaderVo.builder()
                        .messageId(messageId)
                        .messageDate(createdAt.minusMinutes(1))
                        .build())
                .data(Map.of("userId", "user-123"))
                .build())
        .aggregateType(AggregateTypeVo.USER)
        .aggregateId("user-123")
        .action("USER_CREATED")
        .status(status)
        .retryCount(0)
        .createdAt(createdAt)
        .processedAt(null)
        .build();
  }

  private InboxDao toDao(final Inbox inbox) {
    final InboxDao inboxDao = new InboxDao();
    inboxDao.setInboxId(UUID.randomUUID());
    inboxDao.setAggregateType(inbox.aggregateType());
    inboxDao.setAggregateId(inbox.aggregateId());
    inboxDao.setAction(inbox.action());
    inboxDao.setStatus(inbox.status());
    inboxDao.setRetryCount(inbox.retryCount());
    inboxDao.setCreatedAt(inbox.createdAt().toZonedDateTime());

    final MessageHeaderDao messageHeaderDao = new MessageHeaderDao();
    messageHeaderDao.setMessageId(inbox.payload().messageHeader().messageId());
    messageHeaderDao.setMessageDate(
        inbox.payload().messageHeader().messageDate().toZonedDateTime());
    final PayloadDao payloadDao = new PayloadDao();
    payloadDao.setMessageHeader(messageHeaderDao);
    payloadDao.setData(inbox.payload().data());
    inboxDao.setPayload(payloadDao);

    return inboxDao;
  }
}
