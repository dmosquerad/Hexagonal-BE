package com.architecture.hexagonal.application.port.out.inbox;

import com.architecture.hexagonal.domain.model.entity.inbox.Inbox;
import java.util.Optional;
import lombok.NonNull;

public interface InboxRepositoryWritePort {
  Inbox save(@NonNull Inbox inbox);

  Optional<Inbox> claimPendingForProcessing(@NonNull Inbox inbox);

  Inbox upsertByMessageIdentity(@NonNull Inbox inbox);
}
