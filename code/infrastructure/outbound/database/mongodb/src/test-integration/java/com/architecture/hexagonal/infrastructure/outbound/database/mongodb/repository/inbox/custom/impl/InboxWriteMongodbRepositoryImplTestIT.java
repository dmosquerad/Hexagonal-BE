package com.architecture.hexagonal.infrastructure.outbound.database.mongodb.repository.inbox.custom.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.architecture.hexagonal.domain.model.vo.payload.AggregateTypeVo;
import com.architecture.hexagonal.domain.model.vo.payload.StatusVo;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.config.MongodbIT;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.config.MongodbTestApplication;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.data.inbox.InboxDao;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.data.payload.MessageHeaderDao;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.data.payload.PayloadDao;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.repository.inbox.InboxReadMongodbRepository;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.repository.inbox.InboxWriteMongodbRepository;
import java.time.ZonedDateTime;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@ContextConfiguration(classes = MongodbTestApplication.class)
class InboxWriteMongodbRepositoryImplTestIT extends MongodbIT {

  @Autowired private InboxReadMongodbRepository inboxReadMongodbRepository;
  @Autowired private InboxWriteMongodbRepository inboxWriteMongodbRepository;

  @BeforeEach
  void cleanInbox() {
    inboxWriteMongodbRepository.deleteAll();
  }

  @Test
  void saveShouldThrowOnDuplicateMessageIdAndMessageDate() {
    final UUID messageId = UUID.randomUUID();
    final InboxDao original = inbox(messageId, "2026-10-13T12:15:30.123+02:00");
    final InboxDao inserted = inboxWriteMongodbRepository.save(original);

    final InboxDao processed = copyWithStatus(inserted, StatusVo.PROCESSED);
    inboxWriteMongodbRepository.updateByMessageIdentity(processed);

    final InboxDao duplicate = inbox(messageId, "2026-10-13T12:15:30.123+02:00");
    assertThatThrownBy(() -> inboxWriteMongodbRepository.save(duplicate))
        .isInstanceOf(DuplicateKeyException.class);

    assertThat(inboxReadMongodbRepository.count()).isEqualTo(1);
    assertThat(inboxReadMongodbRepository.findAll().get(0).getStatus()).isEqualTo(StatusVo.PROCESSED);

    inboxWriteMongodbRepository.save(
        inbox(
            original.getPayload().getMessageHeader().getMessageId(),
            "2026-10-13T12:15:30.124+02:00"));

    assertThat(inboxReadMongodbRepository.count()).isEqualTo(2);
  }

  @Test
  void claimPendingForProcessingShouldAllowOnlyOneClaim() {
    final InboxDao pending =
        inboxWriteMongodbRepository.save(
            inbox(UUID.randomUUID(), "2026-10-13T10:15:30Z"));
    final InboxDao processing = copyWithStatus(pending, StatusVo.PROCESSING);
    processing.setRetryCount(1);
    processing.setProcessedAt(ZonedDateTime.parse("2026-10-13T10:16:00Z"));

    final InboxDao claimed =
        inboxWriteMongodbRepository.claimPendingForProcessing(processing);
    final InboxDao secondClaim =
        inboxWriteMongodbRepository.claimPendingForProcessing(processing);

    assertThat(claimed.getStatus()).isEqualTo(StatusVo.PROCESSING);
    assertThat(claimed.getRetryCount()).isEqualTo(1);
    assertThat(secondClaim).isNull();
  }

  @Test
  void updateByMessageIdentityShouldThrowWhenInboxDoesNotExist() {
    final InboxDao inboxDao = inbox(UUID.randomUUID(), "2026-10-13T12:15:30.123+02:00");

    assertThatThrownBy(() -> inboxWriteMongodbRepository.updateByMessageIdentity(inboxDao))
        .isInstanceOf(IllegalStateException.class)
        .hasMessageContaining(
            inboxDao.getPayload().getMessageHeader().getMessageId().toString());
  }

  private InboxDao inbox(final UUID messageId, final String messageDate) {
    final InboxDao inboxDao = new InboxDao();
    final MessageHeaderDao messageHeader = new MessageHeaderDao();
    messageHeader.setMessageId(messageId);
    messageHeader.setMessageDate(ZonedDateTime.parse(messageDate));
    final PayloadDao payload = new PayloadDao();
    payload.setMessageHeader(messageHeader);
    payload.setData(java.util.Map.of("name", "Test User"));
    inboxDao.setPayload(payload);
    inboxDao.setAggregateType(AggregateTypeVo.USER);
    inboxDao.setAggregateId("user-123");
    inboxDao.setAction("USER_CREATED");
    inboxDao.setStatus(StatusVo.PENDING);
    inboxDao.setCreatedAt(ZonedDateTime.parse("2026-10-13T10:15:30Z"));
    return inboxDao;
  }

  private InboxDao copyWithStatus(final InboxDao source, final StatusVo status) {
    final InboxDao copy = new InboxDao();
    copy.setInboxId(source.getInboxId());
    copy.setPayload(source.getPayload());
    copy.setAggregateType(source.getAggregateType());
    copy.setAggregateId(source.getAggregateId());
    copy.setAction(source.getAction());
    copy.setStatus(status);
    copy.setRetryCount(source.getRetryCount());
    copy.setCreatedAt(source.getCreatedAt());
    copy.setProcessedAt(source.getProcessedAt());
    return copy;
  }
}
