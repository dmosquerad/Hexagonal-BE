package com.architecture.hexagonal.infrastructure.inbound.rest.mapper.email;

import com.architecture.hexagonal.domain.model.vo.EmailBlockRulesVo;
import com.architecture.hexagonal.infrastructure.contract.rest.email.server.dto.EmailBlockRulesDto;
import com.architecture.hexagonal.infrastructure.inbound.rest.config.MapstructConfig;
import org.mapstruct.Mapper;

@Mapper(config = MapstructConfig.class)
public interface EmailBlockRulesDtoMapper {

  EmailBlockRulesDto toEmailBlockRulesDto(EmailBlockRulesVo source);
}
