package com.architecture.hexagonal.infrastructure.outbound.database.mongodb.integration.adapter.inbox;

import com.architecture.hexagonal.domain.databuilder.model.entity.inbox.InboxTestDataBuilder;
import com.architecture.hexagonal.domain.databuilder.model.vo.date.DateVoTestDataBuilder;
import com.architecture.hexagonal.domain.databuilder.model.vo.payload.MessageHeaderVoTestDataBuilder;
import com.architecture.hexagonal.domain.databuilder.model.vo.payload.PayloadVoTestDataBuilder;
import com.architecture.hexagonal.domain.model.entity.inbox.Inbox;
import com.architecture.hexagonal.domain.model.vo.date.DateVo;
import com.architecture.hexagonal.domain.model.vo.payload.ActionType;
import com.architecture.hexagonal.domain.model.vo.payload.AggregateTypeVo;
import com.architecture.hexagonal.domain.model.vo.payload.StatusVo;

import java.time.Instant;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.adapter.inbox.InboxRepositoryReadMongodbAdapterImpl;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.config.MongodbIT;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.config.MongodbTestApplication;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.databuilder.data.date.DateVoDaoTestDataBuilder;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.databuilder.data.inbox.InboxDaoTestDataBuilder;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.databuilder.data.payload.MessageHeaderDaoTestDataBuilder;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.databuilder.data.payload.PayloadDaoTestDataBuilder;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.repository.inbox.InboxReadMongodbRepository;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.repository.inbox.InboxWriteMongodbRepository;
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
    final DateVo oldestCreatedAt =
        DateVoTestDataBuilder.builder()
            .date(Instant.parse("2026-10-13T10:15:30Z"))
            .zoneId(ZoneId.of("Europe/Madrid"))
            .build()
            .dateVo();
    final Inbox oldest =
        InboxTestDataBuilder.<Object>builder()
            .payload(
                PayloadVoTestDataBuilder.<Object>builder()
                    .messageHeaderVo(
                        MessageHeaderVoTestDataBuilder.builder()
                            .messageId(oldestMessageId)
                            .messageDate(
                                DateVoTestDataBuilder.builder()
                                    .date(oldestCreatedAt.date().minusSeconds(60))
                                    .zoneId(ZoneId.of("Europe/Madrid"))
                                    .build()
                                    .dateVo())
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
    final DateVo newestCreatedAt =
        DateVoTestDataBuilder.builder()
            .date(Instant.parse("2026-10-13T10:17:30Z"))
            .zoneId(ZoneId.of("Europe/Madrid"))
            .build()
            .dateVo();
    final Inbox newest =
        InboxTestDataBuilder.<Object>builder()
            .payload(
                PayloadVoTestDataBuilder.<Object>builder()
                    .messageHeaderVo(
                        MessageHeaderVoTestDataBuilder.builder()
                            .messageId(newestMessageId)
                            .messageDate(
                                DateVoTestDataBuilder.builder()
                                    .date(newestCreatedAt.date().minusSeconds(60))
                                    .zoneId(ZoneId.of("Europe/Madrid"))
                                    .build()
                                    .dateVo())
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
    final DateVo processedCreatedAt =
        DateVoTestDataBuilder.builder()
            .date(Instant.parse("2026-10-13T10:16:30Z"))
            .zoneId(ZoneId.of("Europe/Madrid"))
            .build()
            .dateVo();
    final Inbox processed =
        InboxTestDataBuilder.<Object>builder()
            .payload(
                PayloadVoTestDataBuilder.<Object>builder()
                    .messageHeaderVo(
                        MessageHeaderVoTestDataBuilder.builder()
                            .messageId(processedMessageId)
                            .messageDate(
                                DateVoTestDataBuilder.builder()
                                    .date(processedCreatedAt.date().minusSeconds(60))
                                    .zoneId(ZoneId.of("Europe/Madrid"))
                                    .build()
                                    .dateVo())
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
            .createdAt(
                DateVoDaoTestDataBuilder.builder()
                    .date(newest.createdAt().date())
                    .zoneId(newest.createdAt().zoneId())
                    .build()
                    .dateVoDao())
            .processedAt(null)
            .payload(
                PayloadDaoTestDataBuilder.builder()
                    .messageHeaderDao(
                        MessageHeaderDaoTestDataBuilder.builder()
                            .messageId(newest.payload().messageHeader().messageId())
                            .dateVoDao(
                                DateVoDaoTestDataBuilder.builder()
                                    .date(newest.payload().messageHeader().messageDate().date())
                                    .zoneId(newest.payload().messageHeader().messageDate().zoneId())
                                    .build()
                                    .dateVoDao())
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
                DateVoDaoTestDataBuilder.builder()
                    .date(processed.createdAt().date())
                    .zoneId(processed.createdAt().zoneId())
                    .build()
                    .dateVoDao())
            .processedAt(null)
            .payload(
                PayloadDaoTestDataBuilder.builder()
                    .messageHeaderDao(
                        MessageHeaderDaoTestDataBuilder.builder()
                            .messageId(processed.payload().messageHeader().messageId())
                            .dateVoDao(
                                DateVoDaoTestDataBuilder.builder()
                                    .date(processed.payload().messageHeader().messageDate().date())
                                    .zoneId(processed.payload().messageHeader().messageDate().zoneId())
                                    .build()
                                    .dateVoDao())
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
                DateVoDaoTestDataBuilder.builder()
                    .date(oldest.createdAt().date())
                    .zoneId(oldest.createdAt().zoneId())
                    .build()
                    .dateVoDao())
            .processedAt(null)
            .payload(
                PayloadDaoTestDataBuilder.builder()
                    .messageHeaderDao(
                        MessageHeaderDaoTestDataBuilder.builder()
                            .messageId(oldest.payload().messageHeader().messageId())
                            .dateVoDao(
                                DateVoDaoTestDataBuilder.builder()
                                    .date(oldest.payload().messageHeader().messageDate().date())
                                    .zoneId(oldest.payload().messageHeader().messageDate().zoneId())
                                    .build()
                                    .dateVoDao())
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
