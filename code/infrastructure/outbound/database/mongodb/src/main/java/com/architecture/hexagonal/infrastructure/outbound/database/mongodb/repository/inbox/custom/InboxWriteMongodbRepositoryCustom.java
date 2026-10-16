package com.architecture.hexagonal.infrastructure.outbound.database.mongodb.repository.inbox.custom;

import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.data.inbox.InboxDao;

public interface InboxWriteMongodbRepositoryCustom {
  InboxDao claimPendingForProcessing(InboxDao inboxDao);

  InboxDao updateByMessageIdentity(InboxDao inboxDao);
}
