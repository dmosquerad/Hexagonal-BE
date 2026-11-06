package com.architecture.hexagonal.infrastructure.outbound.database.mongodb.repository.outbox.custom;

import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.data.outbox.OutboxDao;

public interface OutboxWriteMongodbRepositoryCustom {

  OutboxDao upsertByEventIdentity(OutboxDao outboxDao);

  OutboxDao claimPendingForProcessing(OutboxDao outboxDao);
}
