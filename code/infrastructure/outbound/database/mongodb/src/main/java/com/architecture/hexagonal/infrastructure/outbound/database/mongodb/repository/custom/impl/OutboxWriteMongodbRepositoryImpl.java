package com.architecture.hexagonal.infrastructure.outbound.database.mongodb.repository.custom.impl;

import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.data.OutboxDao;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.repository.custom.OutboxWriteMongodbRepositoryCustom;
import java.util.Objects;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

@RequiredArgsConstructor
public class OutboxWriteMongodbRepositoryImpl implements OutboxWriteMongodbRepositoryCustom {

  private final MongoTemplate mongoTemplate;

  @Override
  public OutboxDao findAndSaveWithMerge(OutboxDao outboxDao) {

    Query query =
        new Query(
            Criteria.where(OutboxDao.Fields.aggregateType)
                .is(outboxDao.getAggregateType())
                .and(OutboxDao.Fields.aggregateId)
                .is(outboxDao.getAggregateId())
                .and(OutboxDao.Fields.action)
                .is(outboxDao.getAction())
                .and(OutboxDao.Fields.payload)
                .is(outboxDao.getPayload()));

    Update update = new Update();
    update.setOnInsert(OutboxDao.Fields.outboxId, UUID.randomUUID());
    update.setOnInsert(OutboxDao.Fields.createdAt, outboxDao.getCreatedAt());
    update.set(OutboxDao.Fields.status, outboxDao.getStatus());

    if (outboxDao.getRetryCount() > 0) {
      update.set(OutboxDao.Fields.retryCount, outboxDao.getRetryCount());
    }

    if (Objects.nonNull(outboxDao.getProcessedAt())) {
      update.set(OutboxDao.Fields.processedAt, outboxDao.getProcessedAt());
    }

    return mongoTemplate
        .update(OutboxDao.class)
        .matching(query)
        .apply(update)
        .withOptions(FindAndModifyOptions.options().returnNew(true).upsert(true))
        .findAndModifyValue();
  }
}
