package com.architecture.hexagonal.infrastructure.outbound.database.mongodb.repository.outbox.custom.impl;

import com.architecture.hexagonal.domain.model.entity.user.User;
import com.architecture.hexagonal.domain.model.vo.email.EmailVo;
import com.architecture.hexagonal.domain.model.vo.payload.StatusVo;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.config.MongodbIT;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.config.MongodbTestApplication;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.data.outbox.OutboxDao;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.repository.outbox.OutboxWriteMongodbRepository;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.testutils.data.outbox.OutboxDaoTestDataBuilder;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

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
    void upsertByEventIdentityShouldInsertNewDocumentWhenDocumentDoesNotExist() {
    final OutboxDao outboxDao =
        OutboxDaoTestDataBuilder.builder().retryCount(1).build().outboxDao();

    final OutboxDao result = outboxWriteMongodbRepository.upsertByEventIdentity(outboxDao);

    AssertionsForClassTypes.assertThat(result)
        .usingRecursiveComparison()
        .withEqualsForFields(OutboxWriteMongodbRepositoryImplTestIT::payloadDataEquals, "payload.data")
        .ignoringFieldsOfTypes(UUID.class)
        .isEqualTo(outboxDao);
  }

  @Test
    void upsertByEventIdentityShouldUpdateStatusWhenDocumentExists() {
    final OutboxDao outboxDao =
        OutboxDaoTestDataBuilder.builder().processedAt(null).build().outboxDao();
    final OutboxDao savedOutbox = outboxWriteMongodbRepository.upsertByEventIdentity(outboxDao);

    final OutboxDao updatedOutboxDao =
        OutboxDaoTestDataBuilder.builder()
            .eventId(savedOutbox.getOutboxId())
            .status(StatusVo.PROCESSED)
            .retryCount(1)
            .build()
            .outboxDao();

    final OutboxDao result = outboxWriteMongodbRepository.upsertByEventIdentity(updatedOutboxDao);

    AssertionsForClassTypes.assertThat(result)
        .usingRecursiveComparison()
        .withEqualsForFields(OutboxWriteMongodbRepositoryImplTestIT::payloadDataEquals, "payload.data")
        .ignoringFieldsOfTypes(UUID.class)
        .isEqualTo(updatedOutboxDao);
  }

  @Test
    void upsertByEventIdentityShouldSetRetryCountWhenRetryCountGreaterThanZero() {
    final OutboxDao outboxDao =
        OutboxDaoTestDataBuilder.builder().processedAt(null).build().outboxDao();
    final OutboxDao savedOutbox = outboxWriteMongodbRepository.upsertByEventIdentity(outboxDao);

    final OutboxDao updatedOutboxDao =
        OutboxDaoTestDataBuilder.builder()
            .eventId(savedOutbox.getOutboxId())
            .retryCount(3)
            .build()
            .outboxDao();

    final OutboxDao result = outboxWriteMongodbRepository.upsertByEventIdentity(updatedOutboxDao);

    AssertionsForClassTypes.assertThat(result)
        .usingRecursiveComparison()
        .withEqualsForFields(OutboxWriteMongodbRepositoryImplTestIT::payloadDataEquals, "payload.data")
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
            .outboxDao();
    final OutboxDao savedOutbox = outboxWriteMongodbRepository.upsertByEventIdentity(outboxDao);

    final OutboxDao updatedOutboxDao =
        OutboxDaoTestDataBuilder.builder()
            .eventId(savedOutbox.getOutboxId())
            .status(StatusVo.PROCESSED)
            .retryCount(1)
            .build()
            .outboxDao();

    final OutboxDao result = outboxWriteMongodbRepository.upsertByEventIdentity(updatedOutboxDao);

    AssertionsForClassTypes.assertThat(result)
        .usingRecursiveComparison()
        .withEqualsForFields(OutboxWriteMongodbRepositoryImplTestIT::payloadDataEquals, "payload.data")
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
            .outboxDao();
    outboxWriteMongodbRepository.upsertByEventIdentity(outboxDao);

    final OutboxDao result = outboxWriteMongodbRepository.claimPendingForProcessing(outboxDao);
    final OutboxDao expectedOutboxDao =
        OutboxDaoTestDataBuilder.builder()
            .status(StatusVo.PROCESSING)
            .retryCount(1)
            .build()
            .outboxDao();

    AssertionsForClassTypes.assertThat(result)
        .usingRecursiveComparison()
        .withEqualsForFields(OutboxWriteMongodbRepositoryImplTestIT::payloadDataEquals, "payload.data")
        .ignoringFieldsOfTypes(UUID.class)
        .isEqualTo(expectedOutboxDao);
  }

  @Test
  void claimPendingForProcessingShouldReturnNullWhenEventIsNotPending() {
    final OutboxDao outboxDao = OutboxDaoTestDataBuilder.builder()
            .status(StatusVo.PROCESSING)
            .build()
            .outboxDao();

    outboxWriteMongodbRepository.upsertByEventIdentity(outboxDao);

    final OutboxDao result = outboxWriteMongodbRepository.claimPendingForProcessing(outboxDao);

    AssertionsForClassTypes.assertThat(result).isNull();
  }

  private static boolean payloadDataEquals(final Object actual, final Object expected) {
    if (!(expected instanceof User expectedUser)) {
      return Objects.equals(actual, expected);
    }

    if (actual instanceof User actualUser) {
      return actualUser.equals(expectedUser);
    }

    if (!(actual instanceof Map<?, ?> actualUser)) {
      return false;
    }

    return Objects.equals(String.valueOf(actualUser.get("userId")), expectedUser.userId().toString())
        && Objects.equals(actualUser.get("name"), expectedUser.name())
        && emailDataEquals(actualUser.get("email"), expectedUser.email());
  }

  private static boolean emailDataEquals(final Object actual, final EmailVo expected) {
    if (actual instanceof EmailVo actualEmail) {
      return actualEmail.equals(expected);
    }

    if (!(actual instanceof Map<?, ?> actualEmail)) {
      return false;
    }

    return Objects.equals(actualEmail.get("username"), expected.username())
        && Objects.equals(actualEmail.get("host"), expected.host())
        && Objects.equals(actualEmail.get("tld"), expected.tld());
  }

}
