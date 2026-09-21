package com.architecture.hexagonal.infrastructure.outbound.database.mongodb.repository.custom;

import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.data.OutboxDao;

public interface OutboxWriteMongodbRepositoryCustom {

  OutboxDao findAndSaveWithMerge(OutboxDao outboxDao);
}
