package com.architecture.hexagonal.application.technical.outbox.find.usecase;

import com.architecture.hexagonal.domain.model.aggregate.outbox.Outbox;
import java.util.List;

public interface FindPendingOutboxEventsUseCase {

  List<Outbox> execute();
}
