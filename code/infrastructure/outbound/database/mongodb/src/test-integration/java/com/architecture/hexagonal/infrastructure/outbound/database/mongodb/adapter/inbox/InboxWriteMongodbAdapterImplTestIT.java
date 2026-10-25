package com.architecture.hexagonal.infrastructure.outbound.database.mongodb.adapter.inbox;

import com.architecture.hexagonal.domain.model.entity.inbox.Inbox;
import com.architecture.hexagonal.domain.model.vo.payload.ActionType;
import com.architecture.hexagonal.domain.model.vo.payload.AggregateTypeVo;
import com.architecture.hexagonal.domain.model.vo.payload.MessageHeaderVo;
import com.architecture.hexagonal.domain.model.vo.payload.PayloadVo;
import com.architecture.hexagonal.domain.model.vo.payload.StatusVo;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.config.MongodbIT;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.config.MongodbTestApplication;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.repository.inbox.InboxWriteMongodbRepository;
import java.time.OffsetDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
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
        inbox(UUID.randomUUID(), OffsetDateTime.parse("2026-10-13T10:15:30Z"), StatusVo.PENDING);

    final Inbox result = inboxWriteMongodbAdapter.save(inbox);

    AssertionsForClassTypes.assertThat(result)
        .usingRecursiveComparison()
        .isEqualTo(inbox);
    AssertionsForClassTypes.assertThat(inboxWriteMongodbRepository.count()).isEqualTo(1);
  }

  @Test
  void saveShouldPersistPayloadContainingOffsetDateTime() {
    final Inbox inbox =
        inbox(UUID.randomUUID(), OffsetDateTime.parse("2026-10-13T10:15:30Z"), StatusVo.PENDING);
    final Inbox inboxWithTimestampPayload =
        inbox.toBuilder()
            .payload(
                PayloadVo.builder()
                    .messageHeader(inbox.payload().messageHeader())
                    .data(new TimestampPayload(OffsetDateTime.parse("2026-10-13T10:14:30Z")))
                    .build())
            .build();

    inboxWriteMongodbAdapter.save(inboxWithTimestampPayload);

    AssertionsForClassTypes.assertThat(inboxWriteMongodbRepository.count()).isEqualTo(1);
  }

  @Test
  void claimPendingForProcessingShouldReturnClaimedInbox() {
    final Inbox pending =
        inbox(UUID.randomUUID(), OffsetDateTime.parse("2026-10-13T10:15:30Z"), StatusVo.PENDING);
    inboxWriteMongodbAdapter.save(pending);
    final Inbox processing =
        pending.toBuilder()
            .status(StatusVo.PROCESSING)
            .retryCount(1)
            .processedAt(OffsetDateTime.parse("2026-10-13T10:16:00Z"))
            .build();

    final Optional<Inbox> result =
        inboxWriteMongodbAdapter.claimPendingForProcessing(processing);

    AssertionsForClassTypes.assertThat(result).contains(processing);
  }

  @Test
  void claimPendingForProcessingShouldBeEmptyWhenInboxIsNotPending() {
    final Inbox processing =
        inbox(
            UUID.randomUUID(),
            OffsetDateTime.parse("2026-10-13T10:15:30Z"),
            StatusVo.PROCESSING);
    inboxWriteMongodbAdapter.save(processing);

    final Optional<Inbox> result =
        inboxWriteMongodbAdapter.claimPendingForProcessing(processing);

    AssertionsForClassTypes.assertThat(result).isEmpty();
  }

  @Test
  void upsertByMessageIdentityShouldUpdateInboxStatusAndRetryData() {
    final Inbox pending =
        inbox(UUID.randomUUID(), OffsetDateTime.parse("2026-10-13T10:15:30Z"), StatusVo.PENDING);
    inboxWriteMongodbAdapter.save(pending);
    final Inbox processed =
        pending.toBuilder()
            .status(StatusVo.PROCESSED)
            .retryCount(1)
            .processedAt(OffsetDateTime.parse("2026-10-13T10:16:00Z"))
            .build();

    final Inbox result = inboxWriteMongodbAdapter.upsertByMessageIdentity(processed);

    AssertionsForClassTypes.assertThat(result)
        .usingRecursiveComparison()
        .isEqualTo(processed);
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
        .action(ActionType.UserActionType.USER_CREATED.getActionType())
        .status(status)
        .retryCount(0)
        .createdAt(createdAt)
        .processedAt(null)
        .build();
  }

  private record TimestampPayload(OffsetDateTime timestamp) {}
}
