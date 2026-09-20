package com.architecture.hexagonal.infrastructure.outbound.database.mongodb.repository.custom.impl;

import com.architecture.hexagonal.domain.model.entity.user.User;
import com.architecture.hexagonal.domain.model.vo.outbox.OutboxStatusVo;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.config.MongodbIT;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.config.MongodbTestApplication;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.data.OutboxDao;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.repository.OutboxWriteMongodbRepository;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.testutils.dao.OutboxDaoTestDataBuilder;
import java.util.UUID;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@ContextConfiguration(classes = MongodbTestApplication.class)
class OutboxWriteMongodbRepositoryImplTestIT extends MongodbIT {

  @Autowired private OutboxWriteMongodbRepository outboxWriteMongodbRepository;

  @MockitoSpyBean private MongoTemplate mongoTemplate;

  @Test
  void findAndSaveWithMerge_shouldInsertNewDocument_whenDocumentDoesNotExist() {
    final OutboxDao outboxDao =
        OutboxDaoTestDataBuilder.builder().processedAt(null).build().outboxEventDao();

    final OutboxDao result = outboxWriteMongodbRepository.findAndSaveWithMerge(outboxDao);

    AssertionsForClassTypes.assertThat(result)
        .usingRecursiveComparison()
        .withEqualsForFields(
            (actual, expected) -> new ObjectMapper().convertValue(actual, User.class)
                    .equals(expected),
            "payload.data"
        )
        .ignoringFieldsOfTypes(UUID.class)
        .isEqualTo(outboxDao);
  }

  @Test
  void findAndSaveWithMerge_shouldUpdateStatus_whenDocumentExists() {
    final OutboxDao outboxDao =
        OutboxDaoTestDataBuilder.builder().processedAt(null).build().outboxEventDao();
    final OutboxDao savedOutbox = outboxWriteMongodbRepository.findAndSaveWithMerge(outboxDao);

    final OutboxDao updatedOutboxDao =
        OutboxDaoTestDataBuilder.builder()
            .eventId(savedOutbox.getOutboxId())
            .status(OutboxStatusVo.PUBLISHED)
            .retryCount(1)
            .build()
            .outboxEventDao();

    final OutboxDao result = outboxWriteMongodbRepository.findAndSaveWithMerge(updatedOutboxDao);

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
  void findAndSaveWithMerge_shouldSetRetryCount_whenRetryCountGreaterThanZero() {
    final OutboxDao outboxDao =
        OutboxDaoTestDataBuilder.builder().processedAt(null).build().outboxEventDao();
    final OutboxDao savedOutbox = outboxWriteMongodbRepository.findAndSaveWithMerge(outboxDao);

    final OutboxDao updatedOutboxDao =
        OutboxDaoTestDataBuilder.builder()
            .eventId(savedOutbox.getOutboxId())
            .processedAt(null)
            .retryCount(3)
            .build()
            .outboxEventDao();

    final OutboxDao result = outboxWriteMongodbRepository.findAndSaveWithMerge(updatedOutboxDao);

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
  void findAndSaveWithMerge_shouldUpdateAllFields_whenMultipleFieldsAreModified() {
    final OutboxDao outboxDao =
        OutboxDaoTestDataBuilder.builder()
            .processedAt(null)
            .retryCount(0)
            .status(OutboxStatusVo.PENDING)
            .build()
            .outboxEventDao();
    final OutboxDao savedOutbox = outboxWriteMongodbRepository.findAndSaveWithMerge(outboxDao);

    final OutboxDao updatedOutboxDao =
        OutboxDaoTestDataBuilder.builder()
            .eventId(savedOutbox.getOutboxId())
            .status(OutboxStatusVo.PUBLISHED)
            .retryCount(1)
            .build()
            .outboxEventDao();

    final OutboxDao result = outboxWriteMongodbRepository.findAndSaveWithMerge(updatedOutboxDao);

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

}
