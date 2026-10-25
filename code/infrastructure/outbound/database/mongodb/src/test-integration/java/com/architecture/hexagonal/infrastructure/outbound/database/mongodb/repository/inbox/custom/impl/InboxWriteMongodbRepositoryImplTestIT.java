package com.architecture.hexagonal.infrastructure.outbound.database.mongodb.repository.inbox.custom.impl;

import com.architecture.hexagonal.domain.model.vo.payload.StatusVo;
import com.architecture.hexagonal.domain.testutils.data.time.TestClock;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.config.MongodbIT;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.config.MongodbTestApplication;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.data.inbox.InboxDao;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.data.payload.MessageHeaderDao;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.repository.inbox.InboxReadMongodbRepository;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.repository.inbox.InboxWriteMongodbRepository;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.testutils.data.inbox.InboxDaoTestDataBuilder;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.testutils.data.payload.MessageHeaderDaoTestDataBuilder;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.testutils.data.payload.PayloadDaoTestDataBuilder;
import java.time.ZoneOffset;
import org.assertj.core.api.Assertions;
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
    final InboxDao original = InboxDaoTestDataBuilder.builder().build().inboxDao();
    final InboxDao inserted = inboxWriteMongodbRepository.save(original);

    final InboxDao processed =
        InboxDaoTestDataBuilder.builder()
            .inboxId(inserted.getInboxId())
            .status(StatusVo.PROCESSED)
            .build()
            .inboxDao();
    inboxWriteMongodbRepository.updateByMessageIdentity(processed);

    final InboxDao duplicate = InboxDaoTestDataBuilder.builder().build().inboxDao();
    Assertions.assertThatThrownBy(() -> inboxWriteMongodbRepository.save(duplicate))
        .isInstanceOf(DuplicateKeyException.class);

    Assertions.assertThat(inboxReadMongodbRepository.count()).isEqualTo(1);
    Assertions.assertThat(inboxReadMongodbRepository.findAll().get(0).getStatus())
        .isEqualTo(StatusVo.PROCESSED);

    final MessageHeaderDao nextHeader =
        MessageHeaderDaoTestDataBuilder.builder()
            .messageDate(TestClock.FIXED_INSTANT.plusMillis(1).atZone(ZoneOffset.UTC))
            .build()
            .messageHeaderDao();
    inboxWriteMongodbRepository.save(
        InboxDaoTestDataBuilder.builder()
            .payload(
                PayloadDaoTestDataBuilder.builder()
                    .user(null)
                    .messageHeaderDao(nextHeader)
                    .build()
                    .payloadDao())
            .build()
            .inboxDao());

    Assertions.assertThat(inboxReadMongodbRepository.count()).isEqualTo(2);
  }

  @Test
  void claimPendingForProcessingShouldAllowOnlyOneClaim() {
    final InboxDao pending = inboxWriteMongodbRepository.save(InboxDaoTestDataBuilder.builder().build().inboxDao());
    final InboxDao processing =
        InboxDaoTestDataBuilder.builder()
            .inboxId(pending.getInboxId())
            .status(StatusVo.PROCESSING)
            .retryCount(1)
            .processedAt(TestClock.FIXED_INSTANT.plusSeconds(30).atZone(ZoneOffset.UTC))
            .build()
            .inboxDao();

    final InboxDao claimed =
        inboxWriteMongodbRepository.claimPendingForProcessing(processing);
    final InboxDao secondClaim =
        inboxWriteMongodbRepository.claimPendingForProcessing(processing);

    Assertions.assertThat(claimed.getStatus()).isEqualTo(StatusVo.PROCESSING);
    Assertions.assertThat(claimed.getRetryCount()).isEqualTo(1);
    Assertions.assertThat(secondClaim).isNull();
  }

  @Test
  void updateByMessageIdentityShouldThrowWhenInboxDoesNotExist() {
    final InboxDao inboxDao = InboxDaoTestDataBuilder.builder().build().inboxDao();

    Assertions.assertThatThrownBy(() -> inboxWriteMongodbRepository.updateByMessageIdentity(inboxDao))
        .isInstanceOf(IllegalStateException.class)
        .hasMessageContaining(
            inboxDao.getPayload().getMessageHeader().getMessageId().toString());
  }
}
