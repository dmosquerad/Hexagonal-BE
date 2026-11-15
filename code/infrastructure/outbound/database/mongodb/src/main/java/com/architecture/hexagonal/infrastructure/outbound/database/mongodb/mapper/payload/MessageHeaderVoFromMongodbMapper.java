package com.architecture.hexagonal.infrastructure.outbound.database.mongodb.mapper.payload;

import com.architecture.hexagonal.domain.model.vo.payload.MessageHeaderVo;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.config.MapstructConfig;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.data.payload.MessageHeaderDao;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.mapper.converter.DateVoMongoConverter;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapstructConfig.class, uses = DateVoMongoConverter.class)
public interface MessageHeaderVoFromMongodbMapper {

  @Mapping(
      source = "messageDate",
      target = "messageDate",
      qualifiedByName = DateVoMongoConverter.TO_DATE_VO)
  MessageHeaderVo toMessageHeaderVo(MessageHeaderDao messageHeaderDao);
}
