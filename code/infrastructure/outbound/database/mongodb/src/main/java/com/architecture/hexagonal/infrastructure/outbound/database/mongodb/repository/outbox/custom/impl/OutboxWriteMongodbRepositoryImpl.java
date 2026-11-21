package com.architecture.hexagonal.infrastructure.outbound.database.mongodb.repository.outbox.custom.impl;

import com.architecture.hexagonal.domain.model.vo.payload.StatusVo;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.data.date.DateDao;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.data.outbox.OutboxDao;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.data.payload.MessageHeaderDao;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.data.payload.PayloadDao;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.repository.outbox.custom.OutboxWriteMongodbRepositoryCustom;
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
  public OutboxDao upsertByEventIdentity(final OutboxDao outboxDao) {

    final Query query =
        new Query(
            Criteria.where(OutboxDao.Fields.aggregateType)
                .is(outboxDao.getAggregateType())
                .and(OutboxDao.Fields.aggregateId)
                .is(outboxDao.getAggregateId())
                .and(OutboxDao.Fields.action)
                .is(outboxDao.getAction())
                .and(OutboxDao.Fields.payload + "." + PayloadDao.Fields.data)
                .is(outboxDao.getPayload().getData())
                .and(
                    OutboxDao.Fields.payload
                        + "."
                        + PayloadDao.Fields.messageHeader
                        + "."
                        + MessageHeaderDao.Fields.messageId)
                .is(outboxDao.getPayload().getMessageHeader().getMessageId())
                .and(
                    OutboxDao.Fields.payload
                        + "."
                        + PayloadDao.Fields.messageHeader
                        + "."
                        + MessageHeaderDao.Fields.messageDate
                        + "."
                        + DateDao.Fields.date)
                .is(outboxDao.getPayload().getMessageHeader().getMessageDate().getDate())
                .and(
                    OutboxDao.Fields.payload
                        + "."
                        + PayloadDao.Fields.messageHeader
                        + "."
                        + MessageHeaderDao.Fields.messageDate
                        + "."
                        + DateDao.Fields.zoneId)
                .is(outboxDao.getPayload().getMessageHeader().getMessageDate().getZoneId()));

    final Update update = new Update();
    update.setOnInsert(OutboxDao.Fields.outboxId, UUID.randomUUID());
    update.setOnInsert(OutboxDao.Fields.createdAt, outboxDao.getCreatedAt());
    update.setOnInsert(
        OutboxDao.Fields.payload
            + "."
            + PayloadDao.Fields.messageHeader
            + "."
            + MessageHeaderDao.Fields.messageDate
            + "."
            + DateDao.Fields.zoneId,
        outboxDao.getPayload().getMessageHeader().getMessageDate().getZoneId());
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

  @Override
  public OutboxDao claimPendingForProcessing(final OutboxDao outboxDao) {

    final Query query =
        new Query(
            Criteria.where(OutboxDao.Fields.aggregateType)
                .is(outboxDao.getAggregateType())
                .and(OutboxDao.Fields.aggregateId)
                .is(outboxDao.getAggregateId())
                .and(OutboxDao.Fields.action)
                .is(outboxDao.getAction())
                .and(OutboxDao.Fields.payload + "." + PayloadDao.Fields.data)
                .is(outboxDao.getPayload().getData())
                .and(
                    OutboxDao.Fields.payload
                        + "."
                        + PayloadDao.Fields.messageHeader
                        + "."
                        + MessageHeaderDao.Fields.messageId)
                .is(outboxDao.getPayload().getMessageHeader().getMessageId())
                .and(
                    OutboxDao.Fields.payload
                        + "."
                        + PayloadDao.Fields.messageHeader
                        + "."
                        + MessageHeaderDao.Fields.messageDate
                        + "."
                        + DateDao.Fields.date)
                .is(outboxDao.getPayload().getMessageHeader().getMessageDate().getDate())
                .and(
                    OutboxDao.Fields.payload
                        + "."
                        + PayloadDao.Fields.messageHeader
                        + "."
                        + MessageHeaderDao.Fields.messageDate
                        + "."
                        + DateDao.Fields.zoneId)
                .is(outboxDao.getPayload().getMessageHeader().getMessageDate().getZoneId())
                .and(OutboxDao.Fields.status)
                .is(StatusVo.PENDING));

    final Update update = new Update();
    update.set(OutboxDao.Fields.status, StatusVo.PROCESSING);
    update.set(OutboxDao.Fields.retryCount, outboxDao.getRetryCount());
    update.set(OutboxDao.Fields.processedAt, outboxDao.getProcessedAt());

    return mongoTemplate
        .update(OutboxDao.class)
        .matching(query)
        .apply(update)
        .withOptions(FindAndModifyOptions.options().returnNew(true))
        .findAndModifyValue();
  }
}
