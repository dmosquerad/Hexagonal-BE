package com.architecture.hexagonal.infrastructure.outbound.database.mongodb.integration.adapter.inbox;

import com.architecture.hexagonal.domain.databuilder.model.entity.inbox.InboxTestDataBuilder;
import com.architecture.hexagonal.domain.databuilder.model.vo.payload.MessageHeaderVoTestDataBuilder;
import com.architecture.hexagonal.domain.databuilder.model.vo.payload.PayloadVoTestDataBuilder;
import com.architecture.hexagonal.domain.model.entity.inbox.Inbox;
import com.architecture.hexagonal.domain.model.vo.payload.ActionType;
import com.architecture.hexagonal.domain.model.vo.payload.AggregateTypeVo;
import com.architecture.hexagonal.domain.model.vo.payload.StatusVo;
import com.architecture.hexagonal.domain.model.vo.time.DateVo;
import com.architecture.hexagonal.domain.model.vo.time.factory.DateVoFactory;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.adapter.inbox.InboxRepositoryReadMongodbAdapterImpl;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.config.MongodbIT;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.config.MongodbTestApplication;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.repository.inbox.InboxReadMongodbRepository;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.repository.inbox.InboxWriteMongodbRepository;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.databuilder.data.inbox.InboxDaoTestDataBuilder;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.databuilder.data.payload.MessageHeaderDaoTestDataBuilder;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.databuilder.data.payload.PayloadDaoTestDataBuilder;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.databuilder.data.time.DateDaoTestDataBuilder;
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
class InboxRepositoryReadMongodbAdapterImplTestIT extends MongodbIT {

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
                            .messageDate(DateVoFactory.from(oldestCreatedAt.minusMinutes(1)))
                            .build()
                            .messageHeaderVo())
                    .data(Map.of("userId", "user-123"))
                    .build()
                    .payloadVo())
            .aggregateId("user-123")
            .action(ActionType.UserActionType.USER_CREATED.getActionType())
            .createdAt(DateVoFactory.from(oldestCreatedAt))
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
                            .messageDate(DateVoFactory.from(newestCreatedAt.minusMinutes(1)))
                            .build()
                            .messageHeaderVo())
                    .data(Map.of("userId", "user-123"))
                    .build()
                    .payloadVo())
            .aggregateId("user-123")
            .action(ActionType.UserActionType.USER_CREATED.getActionType())
            .createdAt(DateVoFactory.from(newestCreatedAt))
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
                            .messageDate(DateVoFactory.from(processedCreatedAt.minusMinutes(1)))
                            .build()
                            .messageHeaderVo())
                    .data(Map.of("userId", "user-123"))
                    .build()
                    .payloadVo())
            .aggregateId("user-123")
            .action(ActionType.UserActionType.USER_CREATED.getActionType())
            .status(StatusVo.PROCESSED)
            .createdAt(DateVoFactory.from(processedCreatedAt))
            .processedAt(null)
            .build()
            .inbox();
    inboxWriteMongodbRepository.save(
        InboxDaoTestDataBuilder.builder()
            .inboxId(UUID.randomUUID())
            .aggregateId("user-123")
            .action(newest.action())
            .createdAt(
                DateDaoTestDataBuilder.builder()
                    .date(newest.createdAt().date())
                    .zone(newest.createdAt().zone())
                    .build()
                    .dateDao())
            .processedAt(null)
            .payload(
                PayloadDaoTestDataBuilder.builder()
                    .messageHeaderDao(
                        MessageHeaderDaoTestDataBuilder.builder()
                            .messageId(newest.payload().messageHeader().messageId())
                            .messageDate(
                                DateDaoTestDataBuilder.builder()
                                    .date(
                                        newest
                                            .payload()
                                            .messageHeader()
                                            .messageDate()
                                            .date())
                                    .zone(
                                        newest
                                            .payload()
                                            .messageHeader()
                                            .messageDate()
                                            .zone())
                                    .build()
                                    .dateDao())
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
            .createdAt(
                DateDaoTestDataBuilder.builder()
                    .date(processed.createdAt().date())
                    .zone(processed.createdAt().zone())
                    .build()
                    .dateDao())
            .processedAt(null)
            .payload(
                PayloadDaoTestDataBuilder.builder()
                    .messageHeaderDao(
                        MessageHeaderDaoTestDataBuilder.builder()
                            .messageId(processed.payload().messageHeader().messageId())
                            .messageDate(
                                DateDaoTestDataBuilder.builder()
                                    .date(
                                        processed
                                            .payload()
                                            .messageHeader()
                                            .messageDate()
                                            .date())
                                    .zone(
                                        processed
                                            .payload()
                                            .messageHeader()
                                            .messageDate()
                                            .zone())
                                    .build()
                                    .dateDao())
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
            .createdAt(
                DateDaoTestDataBuilder.builder()
                    .date(oldest.createdAt().date())
                    .zone(oldest.createdAt().zone())
                    .build()
                    .dateDao())
            .processedAt(null)
            .payload(
                PayloadDaoTestDataBuilder.builder()
                    .messageHeaderDao(
                        MessageHeaderDaoTestDataBuilder.builder()
                            .messageId(oldest.payload().messageHeader().messageId())
                            .messageDate(
                                DateDaoTestDataBuilder.builder()
                                    .date(
                                        oldest
                                            .payload()
                                            .messageHeader()
                                            .messageDate()
                                            .date())
                                    .zone(
                                        oldest
                                            .payload()
                                            .messageHeader()
                                            .messageDate()
                                            .zone())
                                    .build()
                                    .dateDao())
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
        .isEqualTo(
            List.of(
                oldest.toBuilder().processedAt(DateVo.builder().build()).build(),
                newest.toBuilder().processedAt(DateVo.builder().build()).build()));
    AssertionsForClassTypes.assertThat(inboxReadMongodbRepository.count()).isEqualTo(3);
  }
}
