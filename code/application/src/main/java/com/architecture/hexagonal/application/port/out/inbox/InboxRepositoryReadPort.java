package com.architecture.hexagonal.application.port.out.inbox;

import com.architecture.hexagonal.domain.model.entity.inbox.Inbox;
import com.architecture.hexagonal.domain.model.vo.payload.AggregateTypeVo;
import com.architecture.hexagonal.domain.model.vo.payload.StatusVo;
import java.util.List;

public interface InboxRepositoryReadPort {
  List<Inbox> findByStatusAndAggregateType(StatusVo status, AggregateTypeVo aggregateType);
}
