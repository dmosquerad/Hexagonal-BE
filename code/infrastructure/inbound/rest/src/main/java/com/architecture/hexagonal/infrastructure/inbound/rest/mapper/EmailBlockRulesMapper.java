package com.architecture.hexagonal.infrastructure.inbound.rest.mapper;

import com.architecture.hexagonal.domain.model.vo.EmailBlockRulesVo;
import com.architecture.hexagonal.infrastructure.inbound.rest.config.MapstructConfig;
import com.architecture.hexagonal.infrastructure.inbound.contract.rest.email.dto.EmailBlockRulesDto;
import org.mapstruct.Mapper;

@Mapper(config = MapstructConfig.class)
public interface EmailBlockRulesMapper {

  EmailBlockRulesDto toEmailBlockRulesDto(EmailBlockRulesVo source);
}

