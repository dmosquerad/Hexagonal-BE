package com.architecture.hexagonal.application.service.technical.inbox;

import com.architecture.hexagonal.domain.model.entity.inbox.Inbox;
import com.architecture.hexagonal.domain.model.vo.payload.AggregateTypeVo;
import java.util.List;
import lombok.NonNull;

public interface InboxService {
  Inbox save(@NonNull Inbox inbox);

  List<Inbox> findPending(@NonNull AggregateTypeVo aggregateType);

  Inbox process(@NonNull Inbox inbox);
}
