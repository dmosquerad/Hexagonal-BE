package com.architecture.hexagonal.application.technical.outbox.find.usecase;

import com.architecture.hexagonal.domain.model.entity.OutboxDo;
import java.util.List;

public interface FindPendingOutboxEventsUseCase {

  List<OutboxDo> execute();
}
