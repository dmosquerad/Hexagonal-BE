package com.architecture.hexagonal.domain.port.out.inbox;

import com.architecture.hexagonal.domain.model.entity.inbox.Inbox;
import lombok.NonNull;

public interface InboxProcess {

  void process(@NonNull Inbox inbox);
}
