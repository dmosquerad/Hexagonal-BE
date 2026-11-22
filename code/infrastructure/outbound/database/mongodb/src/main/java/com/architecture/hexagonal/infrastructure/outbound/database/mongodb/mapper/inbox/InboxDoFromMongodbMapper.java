package com.architecture.hexagonal.infrastructure.outbound.database.mongodb.mapper.inbox;

import com.architecture.hexagonal.domain.model.entity.inbox.Inbox;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.config.MapstructConfig;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.data.inbox.InboxDao;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapstructConfig.class)
public interface InboxDoFromMongodbMapper {

  @Mapping(source = "aggregateId", target = "aggregateId")
  @Mapping(source = "createdAt", target = "createdAt")
  @Mapping(source = "processedAt", target = "processedAt")
  @Mapping(
      source = "payload.messageHeader.messageDate",
      target = "payload.messageHeader.messageDate")
  Inbox toInbox(InboxDao inboxDao);
}
