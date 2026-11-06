package com.architecture.hexagonal.infrastructure.outbound.database.mongodb.repository.inbox;

import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.data.inbox.InboxDao;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.repository.inbox.custom.InboxWriteMongodbRepositoryCustom;
import java.util.UUID;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface InboxWriteMongodbRepository
    extends MongoRepository<InboxDao, UUID>, InboxWriteMongodbRepositoryCustom {

  InboxDao save(InboxDao inboxDao);
}
