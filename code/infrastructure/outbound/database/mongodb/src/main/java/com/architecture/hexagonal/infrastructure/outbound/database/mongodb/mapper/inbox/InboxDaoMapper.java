package com.architecture.hexagonal.infrastructure.outbound.database.mongodb.mapper.inbox;

import com.architecture.hexagonal.domain.model.entity.inbox.Inbox;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.config.MapstructConfig;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.data.inbox.InboxDao;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.mapper.converter.ZoneDateTimeConverter;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapstructConfig.class, uses = ZoneDateTimeConverter.class)
public interface InboxDaoMapper {

  @Mapping(target = "inboxId", ignore = true)
  @Mapping(
      source = "createdAt",
      target = "createdAt",
      qualifiedByName = ZoneDateTimeConverter.TO_ZONED_DATE_TIME)
  @Mapping(
      source = "processedAt",
      target = "processedAt",
      qualifiedByName = ZoneDateTimeConverter.TO_ZONED_DATE_TIME)
  @Mapping(
      source = "payload.messageHeader.messageDate",
      target = "payload.messageHeader.messageDate",
      qualifiedByName = ZoneDateTimeConverter.TO_ZONED_DATE_TIME)
  InboxDao toInboxDao(Inbox inbox);
}
