package com.architecture.hexagonal.infrastructure.outbound.database.mongodb.repository.outbox.custom.impl;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.architecture.hexagonal.domain.model.entity.user.User;
import com.architecture.hexagonal.domain.model.vo.payload.StatusVo;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.config.MongodbIT;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.config.MongodbTestApplication;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.data.outbox.OutboxDao;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.repository.outbox.OutboxWriteMongodbRepository;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.testutils.data.outbox.OutboxDaoTestDataBuilder;
import java.util.UUID;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@ContextConfiguration(classes = MongodbTestApplication.class)
class OutboxWriteMongodbRepositoryImplTestIT extends MongodbIT {

  @Autowired private OutboxWriteMongodbRepository outboxWriteMongodbRepository;

  @MockitoSpyBean private MongoTemplate mongoTemplate;

  @BeforeEach
  void cleanOutbox() {
    outboxWriteMongodbRepository.deleteAll();
  }

  @Test
  void saveShouldThrowOnDuplicateMessageIdAndMessageDate() {
    final OutboxDao original =
        OutboxDaoTestDataBuilder.builder().build().outboxEventDao();
    outboxWriteMongodbRepository.save(original);

    final OutboxDao duplicate =
        OutboxDaoTestDataBuilder.builder().eventId(UUID.randomUUID()).build().outboxEventDao();

    assertThatThrownBy(() -> outboxWriteMongodbRepository.save(duplicate))
        .isInstanceOf(DuplicateKeyException.class);
  }

  @Test
    void upsertByEventIdentityShouldInsertNewDocumentWhenDocumentDoesNotExist() {
    final OutboxDao outboxDao =
        OutboxDaoTestDataBuilder.builder().retryCount(1).build().outboxEventDao();

    final OutboxDao result = outboxWriteMongodbRepository.upsertByEventIdentity(outboxDao);

    AssertionsForClassTypes.assertThat(result)
        .usingRecursiveComparison()
        .withEqualsForFields(
            (actual, expected) -> new ObjectMapper().convertValue(actual, User.class)
                .equals(expected),
            "payload.data")
        .ignoringFieldsOfTypes(UUID.class)
        .isEqualTo(outboxDao);
  }

  @Test
    void upsertByEventIdentityShouldUpdateStatusWhenDocumentExists() {
    final OutboxDao outboxDao =
        OutboxDaoTestDataBuilder.builder().processedAt(null).build().outboxEventDao();
    final OutboxDao savedOutbox = outboxWriteMongodbRepository.upsertByEventIdentity(outboxDao);

    final OutboxDao updatedOutboxDao =
        OutboxDaoTestDataBuilder.builder()
            .eventId(savedOutbox.getOutboxId())
            .status(StatusVo.PROCESSED)
            .retryCount(1)
            .build()
            .outboxEventDao();

    final OutboxDao result = outboxWriteMongodbRepository.upsertByEventIdentity(updatedOutboxDao);

    AssertionsForClassTypes.assertThat(result)
        .usingRecursiveComparison()
        .withEqualsForFields(
            (actual, expected) -> new ObjectMapper().convertValue(actual, User.class)
                .equals(expected),
            "payload.data")
        .ignoringFieldsOfTypes(UUID.class)
        .isEqualTo(updatedOutboxDao);
  }

  @Test
    void upsertByEventIdentityShouldSetRetryCountWhenRetryCountGreaterThanZero() {
    final OutboxDao outboxDao =
        OutboxDaoTestDataBuilder.builder().processedAt(null).build().outboxEventDao();
    final OutboxDao savedOutbox = outboxWriteMongodbRepository.upsertByEventIdentity(outboxDao);

    final OutboxDao updatedOutboxDao =
        OutboxDaoTestDataBuilder.builder()
            .eventId(savedOutbox.getOutboxId())
            .retryCount(3)
            .build()
            .outboxEventDao();

    final OutboxDao result = outboxWriteMongodbRepository.upsertByEventIdentity(updatedOutboxDao);

    AssertionsForClassTypes.assertThat(result)
        .usingRecursiveComparison()
        .withEqualsForFields(
            (actual, expected) -> new ObjectMapper().convertValue(actual, User.class)
                .equals(expected),
            "payload.data"
        )
        .ignoringFieldsOfTypes(UUID.class)
        .isEqualTo(updatedOutboxDao);
  }

  @Test
    void upsertByEventIdentityShouldUpdateAllFieldsWhenMultipleFieldsAreModified() {
    final OutboxDao outboxDao =
        OutboxDaoTestDataBuilder.builder()
            .processedAt(null)
            .retryCount(0)
            .status(StatusVo.PENDING)
            .build()
            .outboxEventDao();
    final OutboxDao savedOutbox = outboxWriteMongodbRepository.upsertByEventIdentity(outboxDao);

    final OutboxDao updatedOutboxDao =
        OutboxDaoTestDataBuilder.builder()
            .eventId(savedOutbox.getOutboxId())
            .status(StatusVo.PROCESSED)
            .retryCount(1)
            .build()
            .outboxEventDao();

    final OutboxDao result = outboxWriteMongodbRepository.upsertByEventIdentity(updatedOutboxDao);

    AssertionsForClassTypes.assertThat(result)
        .usingRecursiveComparison()
        .withEqualsForFields(
            (actual, expected) -> new ObjectMapper().convertValue(actual, User.class)
                .equals(expected),
            "payload.data")
        .ignoringFieldsOfTypes(UUID.class)
        .isEqualTo(updatedOutboxDao);
  }

  @Test
    void claimPendingForProcessingShouldChangeStatusToProcessingWhenPendingEventExists() {
    final OutboxDao outboxDao =
        OutboxDaoTestDataBuilder.builder()
            .status(StatusVo.PENDING)
            .retryCount(1)
            .build()
            .outboxEventDao();
    outboxWriteMongodbRepository.upsertByEventIdentity(outboxDao);

    final OutboxDao result = outboxWriteMongodbRepository.claimPendingForProcessing(outboxDao);
    final OutboxDao expectedOutboxDao =
        OutboxDaoTestDataBuilder.builder()
            .status(StatusVo.PROCESSING)
            .retryCount(1)
            .build()
            .outboxEventDao();

    AssertionsForClassTypes.assertThat(result)
        .usingRecursiveComparison()
        .withEqualsForFields(
            (actual, expected) -> new ObjectMapper().convertValue(actual, User.class)
                .equals(expected),
            "payload.data")
        .ignoringFieldsOfTypes(UUID.class)
        .isEqualTo(expectedOutboxDao);
  }

  @Test
  void claimPendingForProcessingShouldReturnNullWhenEventIsNotPending() {
    final OutboxDao outboxDao = OutboxDaoTestDataBuilder.builder()
            .status(StatusVo.PROCESSING)
            .build()
            .outboxEventDao();

    outboxWriteMongodbRepository.upsertByEventIdentity(outboxDao);

    final OutboxDao result = outboxWriteMongodbRepository.claimPendingForProcessing(outboxDao);

    AssertionsForClassTypes.assertThat(result).isNull();
  }

}
