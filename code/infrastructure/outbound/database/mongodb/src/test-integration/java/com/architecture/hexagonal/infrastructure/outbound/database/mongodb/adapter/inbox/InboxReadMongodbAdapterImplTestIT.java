package com.architecture.hexagonal.infrastructure.outbound.database.mongodb.adapter.inbox;

import com.architecture.hexagonal.domain.model.entity.inbox.Inbox;
import com.architecture.hexagonal.domain.model.vo.payload.ActionType;
import com.architecture.hexagonal.domain.model.vo.payload.AggregateTypeVo;
import com.architecture.hexagonal.domain.model.vo.payload.StatusVo;
import com.architecture.hexagonal.domain.testutils.data.model.entity.inbox.InboxTestDataBuilder;
import com.architecture.hexagonal.domain.testutils.data.model.vo.payload.MessageHeaderVoTestDataBuilder;
import com.architecture.hexagonal.domain.testutils.data.model.vo.payload.PayloadVoTestDataBuilder;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.config.MongodbIT;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.config.MongodbTestApplication;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.repository.inbox.InboxReadMongodbRepository;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.repository.inbox.InboxWriteMongodbRepository;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.testutils.data.inbox.InboxDaoTestDataBuilder;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.testutils.data.payload.MessageHeaderDaoTestDataBuilder;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.testutils.data.payload.PayloadDaoTestDataBuilder;
import java.time.OffsetDateTime;
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
    final UUID oldestMessageId = UUID.randomUUID();
    final OffsetDateTime oldestCreatedAt = OffsetDateTime.parse("2026-10-13T10:15:30Z");
    final Inbox oldest =
        InboxTestDataBuilder.<Object>builder()
            .payload(
                PayloadVoTestDataBuilder.<Object>builder()
                    .messageHeaderVo(
                        MessageHeaderVoTestDataBuilder.builder()
                            .messageId(oldestMessageId)
                            .messageDate(oldestCreatedAt.minusMinutes(1))
                            .build()
                            .messageHeaderVo())
                    .data(Map.of("userId", "user-123"))
                    .build()
                    .payloadVo())
            .aggregateId("user-123")
            .action(ActionType.UserActionType.USER_CREATED.getActionType())
            .createdAt(oldestCreatedAt)
            .processedAt(null)
            .build()
            .inbox();
    final UUID newestMessageId = UUID.randomUUID();
    final OffsetDateTime newestCreatedAt = OffsetDateTime.parse("2026-10-13T10:17:30Z");
    final Inbox newest =
        InboxTestDataBuilder.<Object>builder()
            .payload(
                PayloadVoTestDataBuilder.<Object>builder()
                    .messageHeaderVo(
                        MessageHeaderVoTestDataBuilder.builder()
                            .messageId(newestMessageId)
                            .messageDate(newestCreatedAt.minusMinutes(1))
                            .build()
                            .messageHeaderVo())
                    .data(Map.of("userId", "user-123"))
                    .build()
                    .payloadVo())
            .aggregateId("user-123")
            .action(ActionType.UserActionType.USER_CREATED.getActionType())
            .createdAt(newestCreatedAt)
            .processedAt(null)
            .build()
            .inbox();
    final UUID processedMessageId = UUID.randomUUID();
    final OffsetDateTime processedCreatedAt = OffsetDateTime.parse("2026-10-13T10:16:30Z");
    final Inbox processed =
        InboxTestDataBuilder.<Object>builder()
            .payload(
                PayloadVoTestDataBuilder.<Object>builder()
                    .messageHeaderVo(
                        MessageHeaderVoTestDataBuilder.builder()
                            .messageId(processedMessageId)
                            .messageDate(processedCreatedAt.minusMinutes(1))
                            .build()
                            .messageHeaderVo())
                    .data(Map.of("userId", "user-123"))
                    .build()
                    .payloadVo())
            .aggregateId("user-123")
            .action(ActionType.UserActionType.USER_CREATED.getActionType())
            .status(StatusVo.PROCESSED)
            .createdAt(processedCreatedAt)
            .processedAt(null)
            .build()
            .inbox();
    inboxWriteMongodbRepository.save(
        InboxDaoTestDataBuilder.builder()
            .inboxId(UUID.randomUUID())
            .aggregateId("user-123")
            .action(newest.action())
            .createdAt(newest.createdAt().toZonedDateTime())
            .processedAt(null)
            .payload(
                PayloadDaoTestDataBuilder.builder()
                    .messageHeaderDao(
                        MessageHeaderDaoTestDataBuilder.builder()
                            .messageId(newest.payload().messageHeader().messageId())
                            .messageDate(
                                newest.payload().messageHeader().messageDate().toZonedDateTime())
                            .build()
                            .messageHeaderDao())
                    .data(newest.payload().data())
                    .build()
                    .payloadDao())
            .build()
            .inboxDao());

    inboxWriteMongodbRepository.save(
        InboxDaoTestDataBuilder.builder()
            .inboxId(UUID.randomUUID())
            .aggregateId("user-123")
            .action(processed.action())
            .status(processed.status())
            .createdAt(processed.createdAt().toZonedDateTime())
            .processedAt(null)
            .payload(
                PayloadDaoTestDataBuilder.builder()
                    .messageHeaderDao(
                        MessageHeaderDaoTestDataBuilder.builder()
                            .messageId(processed.payload().messageHeader().messageId())
                            .messageDate(
                                processed.payload()
                                    .messageHeader()
                                    .messageDate()
                                    .toZonedDateTime())
                            .build()
                            .messageHeaderDao())
                    .data(processed.payload().data())
                    .build()
                    .payloadDao())
            .build()
            .inboxDao());

    inboxWriteMongodbRepository.save(
        InboxDaoTestDataBuilder.builder()
            .inboxId(UUID.randomUUID())
            .aggregateId("user-123")
            .action(oldest.action())
            .createdAt(oldest.createdAt().toZonedDateTime())
            .processedAt(null)
            .payload(
                PayloadDaoTestDataBuilder.builder()
                    .messageHeaderDao(
                        MessageHeaderDaoTestDataBuilder.builder()
                            .messageId(oldest.payload().messageHeader().messageId())
                            .messageDate(
                                oldest.payload().messageHeader().messageDate().toZonedDateTime())
                            .build()
                            .messageHeaderDao())
                    .data(oldest.payload().data())
                    .build()
                    .payloadDao())
            .build()
            .inboxDao());

    final List<Inbox> result =
        inboxReadMongodbAdapter.findByStatusAndAggregateType(
            StatusVo.PENDING, AggregateTypeVo.USER);

    AssertionsForClassTypes.assertThat(result)
        .usingRecursiveComparison()
        .isEqualTo(List.of(oldest, newest));
    AssertionsForClassTypes.assertThat(inboxReadMongodbRepository.count()).isEqualTo(3);
  }
}
