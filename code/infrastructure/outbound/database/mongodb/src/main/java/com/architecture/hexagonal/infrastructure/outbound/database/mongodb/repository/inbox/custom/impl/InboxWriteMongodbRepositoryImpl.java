package com.architecture.hexagonal.infrastructure.outbound.database.mongodb.repository.inbox.custom.impl;

import com.architecture.hexagonal.domain.model.vo.payload.StatusVo;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.data.inbox.InboxDao;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.data.payload.MessageHeaderDao;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.data.payload.PayloadDao;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.repository.inbox.custom.InboxWriteMongodbRepositoryCustom;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

@RequiredArgsConstructor
public class InboxWriteMongodbRepositoryImpl implements InboxWriteMongodbRepositoryCustom {

  private final MongoTemplate mongoTemplate;

  @Override
  public InboxDao claimPendingForProcessing(final InboxDao inboxDao) {
    final Query query =
        identityQuery(inboxDao)
            .addCriteria(Criteria.where(InboxDao.Fields.status).is(StatusVo.PENDING));
    final Update update = new Update();
    update.set(InboxDao.Fields.status, StatusVo.PROCESSING);
    update.set(InboxDao.Fields.retryCount, inboxDao.getRetryCount());
    update.set(InboxDao.Fields.processedAt, inboxDao.getProcessedAt());
    return mongoTemplate
        .update(InboxDao.class)
        .matching(query)
        .apply(update)
        .withOptions(FindAndModifyOptions.options().returnNew(true))
        .findAndModifyValue();
  }

  @Override
  public InboxDao updateByMessageIdentity(final InboxDao inboxDao) {
    final Update update = new Update();
    update.set(InboxDao.Fields.status, inboxDao.getStatus());
    update.set(InboxDao.Fields.retryCount, inboxDao.getRetryCount());
    update.set(InboxDao.Fields.processedAt, inboxDao.getProcessedAt());
    final InboxDao updated =
        mongoTemplate
            .update(InboxDao.class)
            .matching(identityQuery(inboxDao))
            .apply(update)
            .withOptions(FindAndModifyOptions.options().returnNew(true))
            .findAndModifyValue();
    if (updated == null) {
      throw new IllegalStateException(
          "Inbox entry not found for messageId="
              + inboxDao.getPayload().getMessageHeader().getMessageId());
    }
    return updated;
  }

  private Query identityQuery(final InboxDao inboxDao) {
    final String payloadMessageHeader =
        InboxDao.Fields.payload + "." + PayloadDao.Fields.messageHeader + ".";
    return new Query(
        Criteria.where(payloadMessageHeader + MessageHeaderDao.Fields.messageId)
            .is(inboxDao.getPayload().getMessageHeader().getMessageId())
            .and(payloadMessageHeader + MessageHeaderDao.Fields.messageDate)
            .is(inboxDao.getPayload().getMessageHeader().getMessageDate()));
  }
}
