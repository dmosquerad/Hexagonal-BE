package com.architecture.hexagonal.application.mapper.date;

import com.architecture.hexagonal.application.config.mapstruct.MapstructConfig;
import com.architecture.hexagonal.application.mapper.converter.DateConverter;
import com.architecture.hexagonal.contract.bus.model.common.Date;
import com.architecture.hexagonal.domain.model.vo.date.DateVo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(config = MapstructConfig.class, uses = DateConverter.class)
public interface DateVoMapper {

  DateVoMapper INSTANCE = Mappers.getMapper(DateVoMapper.class);

  @Mapping(target = "zoneId", source = "zoneId", qualifiedByName = DateConverter.TO_ZONE_ID)
  DateVo toDateVo(Date date);
}
