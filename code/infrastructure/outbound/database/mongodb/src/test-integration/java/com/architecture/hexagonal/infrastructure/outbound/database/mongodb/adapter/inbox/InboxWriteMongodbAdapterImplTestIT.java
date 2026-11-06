package com.architecture.hexagonal.infrastructure.outbound.database.mongodb.adapter.inbox;

import com.architecture.hexagonal.domain.model.entity.inbox.Inbox;
import com.architecture.hexagonal.domain.model.vo.payload.StatusVo;
import com.architecture.hexagonal.domain.testutils.data.model.entity.inbox.InboxTestDataBuilder;
import com.architecture.hexagonal.domain.testutils.data.model.vo.payload.PayloadVoTestDataBuilder;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.config.MongodbIT;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.config.MongodbTestApplication;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.repository.inbox.InboxWriteMongodbRepository;
import java.time.OffsetDateTime;
import java.util.Optional;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest(classes = {InboxRepositoryWriteMongodbAdapterImpl.class})
@ContextConfiguration(classes = MongodbTestApplication.class)
class InboxWriteMongodbAdapterImplTestIT extends MongodbIT {

  @Autowired private InboxRepositoryWriteMongodbAdapterImpl inboxWriteMongodbAdapter;
  @Autowired private InboxWriteMongodbRepository inboxWriteMongodbRepository;

  @BeforeEach
  void cleanInbox() {
    inboxWriteMongodbRepository.deleteAll();
  }

  @Test
  void saveShouldPersistInboxAndReturnMappedEntity() {
    final Inbox inbox =
        InboxTestDataBuilder.<Object>builder().build().inbox();

    final Inbox result = inboxWriteMongodbAdapter.save(inbox);

    AssertionsForClassTypes.assertThat(result)
        .usingRecursiveComparison()
        .isEqualTo(inbox);
    AssertionsForClassTypes.assertThat(inboxWriteMongodbRepository.count()).isEqualTo(1);
  }

  @Test
  void saveShouldPersistPayloadContainingOffsetDateTime() {
    final Inbox inbox = InboxTestDataBuilder.<Object>builder().build().inbox();
    final Inbox inboxWithTimestampPayload =
        inbox.toBuilder()
            .payload(
                PayloadVoTestDataBuilder.<Object>builder()
                    .data(OffsetDateTime.parse("2026-10-13T10:14:30Z"))
                    .build()
                    .payloadVo())
            .build();

    inboxWriteMongodbAdapter.save(inboxWithTimestampPayload);

    AssertionsForClassTypes.assertThat(inboxWriteMongodbRepository.count()).isEqualTo(1);
  }

  @Test
  void claimPendingForProcessingShouldReturnClaimedInbox() {
    final Inbox pending =
        InboxTestDataBuilder.<Object>builder().build().inbox();
    inboxWriteMongodbAdapter.save(pending);
    final Inbox processing =
        pending.toBuilder()
            .status(StatusVo.PROCESSING)
            .retryCount(1)
            .processedAt(pending.createdAt().plusSeconds(30))
            .build();

    final Optional<Inbox> result =
        inboxWriteMongodbAdapter.claimPendingForProcessing(processing);

    AssertionsForClassTypes.assertThat(result).contains(processing);
  }

  @Test
  void claimPendingForProcessingShouldBeEmptyWhenInboxIsNotPending() {
    final Inbox processing =
        InboxTestDataBuilder.<Object>builder()
            .status(StatusVo.PROCESSING)
            .build()
            .inbox();
    inboxWriteMongodbAdapter.save(processing);

    final Optional<Inbox> result =
        inboxWriteMongodbAdapter.claimPendingForProcessing(processing);

    AssertionsForClassTypes.assertThat(result).isEmpty();
  }

  @Test
  void upsertByMessageIdentityShouldUpdateInboxStatusAndRetryData() {
    final Inbox pending =
        InboxTestDataBuilder.<Object>builder().build().inbox();
    inboxWriteMongodbAdapter.save(pending);
    final Inbox processed =
        pending.toBuilder()
            .status(StatusVo.PROCESSED)
            .retryCount(1)
            .processedAt(pending.createdAt().plusSeconds(30))
            .build();

    final Inbox result = inboxWriteMongodbAdapter.upsertByMessageIdentity(processed);

    AssertionsForClassTypes.assertThat(result)
        .usingRecursiveComparison()
        .isEqualTo(processed);
  }
}
