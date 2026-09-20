package com.architecture.hexagonal.infrastructure.outbound.database.mongodb.repository;

import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.data.OutboxDao;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.repository.custom.OutboxWriteMongodbRepositoryCustom;
import java.util.UUID;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface OutboxWriteMongodbRepository
    extends MongoRepository<OutboxDao, UUID>, OutboxWriteMongodbRepositoryCustom {

  OutboxDao save(OutboxDao outboxDao);
}
