package com.architecture.hexagonal.infrastructure.outbound.database.mongodb.adapter.inbox;

import com.architecture.hexagonal.application.port.out.inbox.InboxRepositoryWritePort;
import com.architecture.hexagonal.domain.model.entity.inbox.Inbox;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.mapper.inbox.InboxDaoMapper;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.mapper.inbox.InboxDoFromMongodbMapper;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.repository.inbox.InboxWriteMongodbRepository;
import java.util.Optional;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class InboxRepositoryWriteMongodbAdapterImpl implements InboxRepositoryWritePort {

  private final InboxWriteMongodbRepository inboxWriteMongodbRepository;
  private final InboxDaoMapper inboxDaoMapper;
  private final InboxDoFromMongodbMapper inboxDoFromMongodbMapper;

  @Override
  public Inbox save(final @NonNull Inbox inbox) {
    return inboxDoFromMongodbMapper.toInbox(
        inboxWriteMongodbRepository.save(inboxDaoMapper.toInboxDao(inbox)));
  }

  @Override
  public Optional<Inbox> claimPendingForProcessing(final @NonNull Inbox inbox) {
    return Optional.ofNullable(
            inboxWriteMongodbRepository.claimPendingForProcessing(inboxDaoMapper.toInboxDao(inbox)))
        .map(inboxDoFromMongodbMapper::toInbox);
  }

  @Override
  public Inbox upsertByMessageIdentity(final @NonNull Inbox inbox) {
    return inboxDoFromMongodbMapper.toInbox(
        inboxWriteMongodbRepository.updateByMessageIdentity(inboxDaoMapper.toInboxDao(inbox)));
  }
}
