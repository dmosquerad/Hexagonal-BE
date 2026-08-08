package com.architecture.hexagonal.application.port.database;

import com.architecture.hexagonal.domain.model.entity.OutboxDo;
import java.util.List;

public interface OutboxRepositoryReadPort {
  List<OutboxDo> findPendingEvents();
}
