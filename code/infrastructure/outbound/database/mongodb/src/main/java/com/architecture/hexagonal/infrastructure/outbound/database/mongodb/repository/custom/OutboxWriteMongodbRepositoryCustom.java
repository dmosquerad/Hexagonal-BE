package com.architecture.hexagonal.infrastructure.outbound.database.mongodb.repository.custom;

import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.data.OutboxDao;

public interface OutboxWriteMongodbRepositoryCustom {

  OutboxDao upsertByEventIdentity(OutboxDao outboxDao);

  OutboxDao claimPendingForProcessing(OutboxDao outboxDao);
}
